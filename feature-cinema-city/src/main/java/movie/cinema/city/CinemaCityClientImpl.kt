package movie.cinema.city

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.basicAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import movie.cinema.city.model.BodyResponse
import movie.cinema.city.model.BookingDetailResponse
import movie.cinema.city.model.BookingResponse
import movie.cinema.city.model.CinemaResponse
import movie.cinema.city.model.CustomerDataRequest
import movie.cinema.city.model.CustomerPointsResponse
import movie.cinema.city.model.CustomerResponse
import movie.cinema.city.model.ExtendedMovieResponse
import movie.cinema.city.model.MovieDetailResponse
import movie.cinema.city.model.MovieDetailsResponse
import movie.cinema.city.model.MovieEventResponse
import movie.cinema.city.model.PasswordRequest
import movie.cinema.city.model.PromoCardResponse
import movie.cinema.city.model.RegistrationRequest
import movie.cinema.city.model.ResultsResponse
import movie.cinema.city.model.ShowingType
import movie.cinema.city.model.TokenRequest
import movie.cinema.city.model.TokenResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal class CinemaCityClientImpl(
    private val auth: CinemaCityAuth,
    private val provider: EndpointProvider,
    private val account: CredentialsProvider
) : CinemaCityClient {

    private var tokens = null as BearerTokens?
    private val root = httpClient {
        defaultRequest {
            url(provider.domain + "/")
        }
    }

    private val customer = httpClient {
        defaultRequest {
            url(provider.domain + "/")
        }
        install(Auth) {
            bearer {
                loadTokens { tokens }
                refreshTokens {
                    val request = when (val t = oldTokens) {
                        null -> account.get().run { TokenRequest.Login(username, password) }
                        else -> TokenRequest.Refresh(t.refreshToken, auth.captcha)
                    }
                    val params = Parameters.build {
                        appendAll(request.toParameters())
                        append("reCaptcha", auth.captcha)
                    }
                    val response = client.submitForm(params) {
                        url("${provider.tld}/group-customer-service/oauth/token")
                        basicAuth(auth.user, auth.pass)
                    }
                    response.body<TokenResponse>().run {
                        BearerTokens(accessToken, refreshToken).also { tokens = it }
                    }
                }
            }
        }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)

    override suspend fun register(request: RegistrationRequest): CustomerResponse {
        return root.post {
            url("${provider.tld}/group-customer-service/v1/customers")
            parameter("reCaptcha", auth.captcha)
            basicAuth(auth.user, auth.pass)
            setBody(request)
        }.body<CustomerResponse>().also {
            val token = it.token ?: return@also
            tokens = BearerTokens(token.accessToken, token.refreshToken)
        }
    }

    override suspend fun updatePassword(request: PasswordRequest) {
        return customer.put {
            url("${provider.tld}/group-customer-service/v1/password")
            parameter("reCaptcha", auth.captcha)
            setBody(request)
        }.body()
    }

    override suspend fun updateUser(request: CustomerDataRequest): CustomerResponse {
        return customer.put {
            url("${provider.tld}/group-customer-service/v1/customers/current")
            setBody(request)
        }.body()
    }

    override suspend fun getPoints(): CustomerPointsResponse {
        return customer.get {
            url("${provider.tld}/group-customer-service/v1/customer/points")
        }.body()
    }

    override suspend fun getUser(): CustomerResponse.Customer {
        return customer.get {
            url("${provider.tld}/group-customer-service/v1/customers/current")
        }.body()
    }

    override suspend fun getBookings(): List<BookingResponse> {
        return customer.get {
            url("${provider.tld}/group-customer-service/v1/bookings")
            parameter("lang", Locale.getDefault().language)
        }.body()
    }

    override suspend fun getBooking(id: String): BookingDetailResponse {
        return customer.get {
            url("${provider.tld}/group-customer-service/v1/bookings/$id")
            parameter("lang", Locale.getDefault().language)
        }.body()
    }

    override suspend fun getCinemas(): List<CinemaResponse> {
        return root.get {
            url("mrest/cinema")
            parameter("lang", Locale.getDefault().language)
        }.body<ResultsResponse<List<CinemaResponse>>>().results.map {
            it.copy(image = "${provider.domain}/magnoliaPublic/dam/${it.image}")
        }
    }

    override suspend fun getPromoCards(): List<PromoCardResponse> {
        return root.get {
            url("mrest/special-promo-cards")
            parameter("lang", Locale.getDefault().language)
        }.body<ResultsResponse<List<PromoCardResponse>>>().results.asSequence()
            .filter { it.enabled }
            .filter { Date() in it.start..it.end }
            .map { it.copy(image = "${provider.domain}/magnoliaPublic/dam/${it.image}") }
            .toList()
    }

    override suspend fun getEventsInCinema(cinema: String, date: Date): MovieEventResponse {
        return root.get {
            val atDate = formatter.format(date)
            url("${provider.tld}/data-api-service/v1/quickbook/${provider.id}/film-events/in-cinema/$cinema/at-date/$atDate")
        }.body<BodyResponse<MovieEventResponse>>().body
    }

    override suspend fun getDetail(id: String): MovieDetailResponse {
        return root.get {
            url("${provider.tld}/data-api-service/v1/${provider.id}/films/byDistributorCode/$id")
            parameter("lang", Locale.getDefault().language)
        }.body<BodyResponse<MovieDetailsResponse>>().body.details
    }

    override suspend fun getMoviesByType(type: ShowingType): List<ExtendedMovieResponse> {
        return root.get {
            url("${provider.tld}/data-api-service/v1/${provider.id}/films/by-showing-type/${type.value}")
            parameter("ordering", "asc")
            parameter("lang", Locale.getDefault().language)
        }.body<BodyResponse<List<ExtendedMovieResponse>>>().body
    }

    // ---

    private fun httpClient(builder: HttpClientConfig<*>.() -> Unit) = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                explicitNulls = false
            })
        }
        install(HttpCache)
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        builder()
    }

}