package movie.core.nwk

import movie.core.auth.UserAccount
import movie.core.auth.UserCredentials
import movie.core.nwk.model.TokenRequest

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