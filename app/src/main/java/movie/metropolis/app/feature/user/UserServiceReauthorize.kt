package movie.metropolis.app.feature.user

import movie.metropolis.app.feature.user.model.CustomerDataRequest
import movie.metropolis.app.feature.user.model.PasswordRequest
import movie.metropolis.app.feature.user.model.TokenRequest
import kotlin.time.Duration.Companion.minutes

internal class UserServiceReauthorize(
    private val origin: UserService,
    private val credentials: UserCredentials,
    private val account: UserAccount
) : UserService by origin {

    private val refreshToken
        get() = checkNotNull(account.refreshToken)

    private val email
        get() = checkNotNull(credentials.email)

    private val password
        get() = checkNotNull(credentials.password)

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

    private suspend inline fun <T> requireToken(body: () -> Result<T>): Result<T> {
        if (account.expiresWithin(1.minutes)) {
            val result = getToken(TokenRequest.Refresh(refreshToken))
            if (result.isFailure) return result as Result<T>
        } else if (account.isExpired) {
            val result = getToken(TokenRequest.Login(email, password))
            if (result.isFailure) return result as Result<T>
        }
        return body()
    }

}