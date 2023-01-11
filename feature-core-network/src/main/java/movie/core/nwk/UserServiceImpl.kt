package movie.core.nwk

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
import io.ktor.http.Parameters
import movie.core.auth.UserAccount
import movie.core.nwk.model.BookingDetailResponse
import movie.core.nwk.model.BookingResponse
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.CustomerPointsResponse
import movie.core.nwk.model.CustomerResponse
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest
import movie.core.nwk.model.TokenResponse
import java.util.Locale

internal class UserServiceImpl(
    private val client: HttpClient,
    private val account: UserAccount,
    private val authUser: String,
    private val authPass: String,
    private val authCaptcha: String
) : UserService {

    override suspend fun register(request: RegistrationRequest) = client.runCatching {
        post {
            url("v1/customers")
            parameter("reCaptcha", authCaptcha)
            setBody(request)
            basicAuth(authUser, authPass)
        }.requireBody<CustomerResponse>()
    }

    override suspend fun getToken(request: TokenRequest) = client.runCatching {
        val params = Parameters.build {
            appendAll(request.toParameters())
            append("reCaptcha", authCaptcha)
        }
        submitForm(params) {
            url("oauth/token")
            basicAuth(authUser, authPass)
        }.requireBody<TokenResponse>()
    }

    override suspend fun getCurrentToken() = client.runCatching {
        requireNotNull(account.token)
    }

    override suspend fun updatePassword(request: PasswordRequest) = client.runCatching {
        put {
            url("v1/password")
            parameter("reCaptcha", authCaptcha)
            setBody(request)
            bearerAuth(checkNotNull(account.token))
        }.requireBody<Unit>()
    }

    override suspend fun updateUser(request: CustomerDataRequest) =
        client.runCatching {
            put {
                url("v1/customers/current")
                bearerAuth(checkNotNull(account.token))
            }.requireBody<CustomerResponse>()
        }

    override suspend fun getPoints() = client.runCatching {
        get {
            url("v1/customer/points")
            bearerAuth(checkNotNull(account.token))
        }.requireBody<CustomerPointsResponse>()
    }

    override suspend fun getUser() = client.runCatching {
        get {
            url("v1/customers/current")
            bearerAuth(checkNotNull(account.token))
        }.requireBody<CustomerResponse.Customer>()
    }

    override suspend fun getBookings() = client.runCatching {
        get {
            url("v1/bookings")
            bearerAuth(checkNotNull(account.token))
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }.requireBody<List<BookingResponse>>()
    }

    override suspend fun getBooking(id: String) = client.runCatching {
        get {
            url("v1/bookings/$id")
            bearerAuth(checkNotNull(account.token))
            url {
                parameters.append("lang", Locale.getDefault().language)
            }
        }.requireBody<BookingDetailResponse>()
    }

}