package movie.metropolis.app.feature.global

import movie.metropolis.app.feature.global.model.remote.TokenRequest
import movie.metropolis.app.util.NetworkException

internal class UserServiceLogout(
    private val origin: UserService,
    private val credentials: UserCredentials,
    private val account: UserAccount
) : UserService by origin {

    override suspend fun getToken(
        request: TokenRequest
    ) = origin.getToken(request).onFailure {
        if (it !is NetworkException) return@onFailure
        if (it.code != 400) return@onFailure

        account.delete()
        credentials.email = null
        credentials.password = null
    }

}