package movie.metropolis.app.feature.global

import io.ktor.client.HttpClient
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import movie.metropolis.app.feature.global.model.remote.BookingDetailResponse
import movie.metropolis.app.feature.global.model.remote.BookingResponse
import movie.metropolis.app.feature.global.model.remote.CustomerDataRequest
import movie.metropolis.app.feature.global.model.remote.CustomerPointsResponse
import movie.metropolis.app.feature.global.model.remote.CustomerResponse
import movie.metropolis.app.feature.global.model.remote.PasswordRequest
import movie.metropolis.app.feature.global.model.remote.RegistrationRequest
import movie.metropolis.app.feature.global.model.remote.TokenRequest
import movie.metropolis.app.feature.global.model.remote.TokenResponse
import movie.metropolis.app.util.requireBody
import java.util.Locale

internal class UserServiceImpl(
    private val client: HttpClient,
    private val account: UserAccount,
    private val authUser: String,
    private val authPass: String,
    private val authCaptcha: String
) : UserService {

    override suspend fun register(request: RegistrationRequest) = kotlin.runCatching {
        client.post {
            url("v1/customers")
            parameter("reCaptcha", authCaptcha)
            setBody(request)
            basicAuth(authUser, authPass)
        }.requireBody<CustomerResponse>()
    }

    override suspend fun getToken(request: TokenRequest) = kotlin.runCatching {
        client.submitForm(request.toParameters()) {
            url("oauth/token")
            basicAuth(authUser, authPass)
        }.requireBody<TokenResponse>()
    }

    override suspend fun updatePassword(request: PasswordRequest) = kotlin.runCatching {
        client.put {
            url("v1/password")
            parameter("reCaptcha", authCaptcha)
            setBody(request)
            bearerAuth(checkNotNull(account.token))
        }.requireBody<Unit>()
    }

    override suspend fun updateUser(request: CustomerDataRequest) = kotlin.runCatching {
        client.put {
            url("v1/customers/current")
            bearerAuth(checkNotNull(account.token))
        }.requireBody<CustomerResponse>()
    }

    override suspend fun getPoints() = kotlin.runCatching {
        client.get {
            url("v1/customer/points")
            bearerAuth(checkNotNull(account.token))
        }.requireBody<CustomerPointsResponse>()
    }

    override suspend fun getUser() = kotlin.runCatching {
        client.get {
            url("v1/customers/current")
            bearerAuth(checkNotNull(account.token))
        }.requireBody<CustomerResponse.Customer>()
    }

    override suspend fun getBookings() = kotlin.runCatching {
        client.get {
            url("v1/bookings")
            bearerAuth(checkNotNull(account.token))
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }.requireBody<List<BookingResponse>>()
    }

    override suspend fun getBooking(id: String) = kotlin.runCatching {
        client.get {
            url("v1/bookings/$id")
            bearerAuth(checkNotNull(account.token))
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }.requireBody<BookingDetailResponse>()
    }

}