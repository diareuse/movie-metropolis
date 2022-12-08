package movie.core.nwk

import movie.core.auth.UserAccount
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.TokenRequest
import kotlin.time.Duration.Companion.minutes

internal class UserServiceReauthorize(
    private val origin: UserService,
    private val account: UserAccount,
    private val captcha: String
) : UserService by origin {

    private val refreshToken
        get() = checkNotNull(account.refreshToken)

    private val email
        get() = checkNotNull(account.email)

    private val password
        get() = checkNotNull(account.password)

    override suspend fun getCurrentToken() = requireToken {
        origin.getCurrentToken()
    }

    override suspend fun updatePassword(request: PasswordRequest) = requireToken {
        origin.updatePassword(request)
    }

    override suspend fun updateUser(request: CustomerDataRequest) = requireToken {
        origin.updateUser(request)
    }

    override suspend fun getPoints() = requireToken {
        origin.getPoints()
    }

    override suspend fun getUser() = requireToken {
        origin.getUser()
    }

    override suspend fun getBookings() = requireToken {
        origin.getBookings()
    }

    override suspend fun getBooking(id: String) = requireToken {
        origin.getBooking(id)
    }

    private suspend inline fun <T> requireToken(body: () -> Result<T>): Result<T> {
        if (!account.isLoggedIn) {
            return Result.failure(SecurityException())
        } else if (account.expiresWithin(1.minutes)) {
            val result = getToken(TokenRequest.Refresh(token = refreshToken, captcha = captcha))
            if (result.isFailure) return result as Result<T>
        } else if (account.isExpired) {
            val result = getToken(TokenRequest.Login(username = email, password = password))
            if (result.isFailure) return result as Result<T>
        }
        return body()
    }

}