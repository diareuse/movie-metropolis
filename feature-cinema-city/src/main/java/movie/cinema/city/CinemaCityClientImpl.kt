package movie.cinema.city

import io.ktor.client.HttpClient
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
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.Parameters
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.http.isSuccess
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
    private val provider: RegionProvider,
    private val account: CredentialsProvider,
    private val tokenStore: TokenStore
) : CinemaCityClient {

    private val client by lazy {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                })
            }
            install(HttpCache)
            defaultRequest {
                contentType(ContentType.Application.Json)
                url(provider.domain + "/")
            }
            install(Auth) {
                bearer {
                    sendWithoutRequest {
                        val path = it.url.encodedPath
                        var matching = path.contains("group-customer-service")
                        matching = matching and !path.contains("oauth/token")
                        matching
                    }
                    loadTokens { BearerTokens(tokenStore.token, tokenStore.refreshToken) }
                    refreshTokens {
                        val t = oldTokens?.takeUnless {
                            it.accessToken.isBlank() || it.refreshToken.isBlank()
                        }
                        val request = when (t) {
                            null -> try {
                                account.get().run { TokenRequest.Login(username, password) }
                            } catch (e: Throwable) {
                                return@refreshTokens null
                            }

                            else -> TokenRequest.Refresh(t.refreshToken, auth.captcha)
                        }
                        client.getToken(request)
                    }
                }
            }
        }
    }

    private val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override suspend fun register(request: RegistrationRequest): CustomerResponse {
        return this.client.post {
            url("${provider.tld}/group-customer-service/v1/customers")
            parameter("reCaptcha", auth.captcha)
            basicAuth(auth.user, auth.pass)
            setBody(request)
        }.throwOnError().body<CustomerResponse>().also {
            val response = it.token ?: return@also
            tokenStore.token = response.accessToken
            tokenStore.refreshToken = response.refreshToken
        }
    }

    override suspend fun updatePassword(request: PasswordRequest) {
        return this.client.put {
            url("${provider.tld}/group-customer-service/v1/password")
            parameter("reCaptcha", auth.captcha)
            setBody(request)
        }.throwOnError().body()
    }

    override suspend fun updateUser(request: CustomerDataRequest): CustomerResponse {
        return this.client.put {
            url("${provider.tld}/group-customer-service/v1/customers/current")
            setBody(request)
        }.throwOnError().body()
    }

    override suspend fun getPoints(): CustomerPointsResponse {
        return this.client.get {
            url("${provider.tld}/group-customer-service/v1/customer/points")
        }.throwOnError().body()
    }

    override suspend fun getUser(): CustomerResponse.Customer {
        return this.client.get {
            url("${provider.tld}/group-customer-service/v1/customers/current")
        }.throwOnError().body()
    }

    override suspend fun getBookings(): List<BookingResponse> {
        return this.client.get {
            url("${provider.tld}/group-customer-service/v1/bookings")
            parameter("lang", Locale.getDefault().language)
        }.throwOnError().body()
    }

    override suspend fun getBooking(id: String): BookingDetailResponse {
        return this.client.get {
            url("${provider.tld}/group-customer-service/v1/bookings/$id")
            parameter("lang", Locale.getDefault().language)
        }.throwOnError().body()
    }

    override suspend fun getCinemas(): List<CinemaResponse> {
        return this.client.get {
            url("mrest/cinema")
            parameter("lang", Locale.getDefault().language)
        }.throwOnError().body<ResultsResponse<List<CinemaResponse>>>().results.map {
            it.copy(image = "${provider.domain}/magnoliaPublic/dam/${it.image}")
        }
    }

    override suspend fun getPromoCards(): List<PromoCardResponse> {
        return this.client.get {
            url("mrest/special-promo-cards")
            parameter("lang", Locale.getDefault().language)
        }.throwOnError().body<ResultsResponse<List<PromoCardResponse>>>().results.asSequence()
            .filter { it.enabled }
            .filter { Date() in it.start..it.end }
            .map { it.copy(image = "${provider.domain}/magnoliaPublic/dam/${it.image}") }
            .toList()
    }

    override suspend fun getEventsInCinema(cinema: String, date: Date): MovieEventResponse {
        return this.client.get {
            val atDate = formatter.format(date)
            url("${provider.tld}/data-api-service/v1/quickbook/${provider.id}/film-events/in-cinema/$cinema/at-date/$atDate")
        }.throwOnError().body<BodyResponse<MovieEventResponse>>().body
    }

    override suspend fun getDetail(id: String): MovieDetailResponse {
        return this.client.get {
            url("${provider.tld}/data-api-service/v1/${provider.id}/films/byDistributorCode/$id")
            parameter("lang", Locale.getDefault().language)
        }.throwOnError().body<BodyResponse<MovieDetailsResponse>>().body.details
    }

    override suspend fun getMoviesByType(type: ShowingType): List<ExtendedMovieResponse> {
        return this.client.get {
            url("${provider.tld}/data-api-service/v1/${provider.id}/films/by-showing-type/${type.value}")
            parameter("ordering", "asc")
            parameter("lang", Locale.getDefault().language)
        }.throwOnError().body<BodyResponse<List<ExtendedMovieResponse>>>().body
    }

    override suspend fun getToken(): BearerTokens {
        return this.client.getToken(account.get().run { TokenRequest.Login(username, password) })
    }

    private suspend fun HttpClient.getToken(request: TokenRequest): BearerTokens {
        val params = Parameters.build {
            appendAll(request.toParameters())
            if (!contains("reCaptcha"))
                append("reCaptcha", auth.captcha)
        }
        val response = submitForm(params) {
            headers.remove("Authorization")
            url("${provider.tld}/group-customer-service/oauth/token")
            basicAuth(auth.user, auth.pass)
        }
        if (!response.status.isSuccess() && request is TokenRequest.Refresh) {
            // how retarded must you be to provide refresh token which expires? spec clearly says
            // that very presence of the refresh token guarantees getting new token, not getting
            // a fucking 401 when trying to using it after access token expires. what's the fucking
            // point of the refresh token then…?
            return getToken(account.get().run { TokenRequest.Login(username, password) })
        }
        val token = response.throwOnError().body<TokenResponse>()
        return BearerTokens(token.accessToken, token.refreshToken).also {
            tokenStore.token = it.accessToken
            tokenStore.refreshToken = it.refreshToken
        }
    }

    // ---

    private suspend fun HttpResponse.throwOnError() = apply {
        if (!status.isSuccess()) {
            throw HttpException(status.value, request.url.toString(), bodyAsText())
        }
    }

}