package movie.metropolis.app.feature.user

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.basicAuth
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import movie.metropolis.app.BuildConfig
import movie.metropolis.app.feature.user.model.BookingResponse
import movie.metropolis.app.feature.user.model.CustomerDataRequest
import movie.metropolis.app.feature.user.model.CustomerPointsResponse
import movie.metropolis.app.feature.user.model.CustomerResponse
import movie.metropolis.app.feature.user.model.PasswordRequest
import movie.metropolis.app.feature.user.model.RegistrationRequest
import movie.metropolis.app.feature.user.model.TokenRequest
import movie.metropolis.app.feature.user.model.TokenResponse

internal class UserServiceImpl(
    private val client: HttpClient,
    private val account: UserAccount
) : UserService {

    override suspend fun register(request: RegistrationRequest) = kotlin.runCatching {
        client.post {
            url("/v1/customers")
            parameter("reCaptcha", BuildConfig.Captcha)
            setBody(request)
            basicAuth(BuildConfig.BasicUser, BuildConfig.BasicPass)
        }.body<CustomerResponse>()
    }

    override suspend fun getToken(request: TokenRequest) = kotlin.runCatching {
        client.submitForm(request.toParameters()) {
            url("/oauth/token")
            basicAuth(BuildConfig.BasicUser, BuildConfig.BasicPass)
        }.body<TokenResponse>()
    }

    override suspend fun updatePassword(request: PasswordRequest) = kotlin.runCatching {
        client.put {
            url("/v1/password")
            parameter("reCaptcha", BuildConfig.Captcha)
            setBody(request)
            bearerAuth(checkNotNull(account.token))
        }.body<Unit>()
    }

    override suspend fun updateUser(request: CustomerDataRequest) = kotlin.runCatching {
        client.put {
            url("/v1/customers/current")
            bearerAuth(checkNotNull(account.token))
        }.body<CustomerResponse>()
    }

    override suspend fun getPoints() = kotlin.runCatching {
        client.get {
            url("/v1/customer/points")
            bearerAuth(checkNotNull(account.token))
        }.body<CustomerPointsResponse>()
    }

    override suspend fun getUser() = kotlin.runCatching {
        client.get {
            url("/v1/customers/current")
            bearerAuth(checkNotNull(account.token))
        }.body<CustomerResponse.Customer>()
    }

    override suspend fun getBookings() = kotlin.runCatching {
        client.get {
            url("/v1/bookings")
            bearerAuth(checkNotNull(account.token))
        }.body<List<BookingResponse>>()
    }

}