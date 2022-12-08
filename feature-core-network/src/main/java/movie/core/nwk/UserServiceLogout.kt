package movie.core.nwk

import movie.core.auth.UserAccount
import movie.core.nwk.model.TokenRequest

internal class UserServiceLogout(
    private val origin: UserService,
    private val account: UserAccount
) : UserService by origin {

    override suspend fun getToken(
        request: TokenRequest
    ) = origin.getToken(request).onFailure {
        if (it !is NetworkException) return@onFailure
        if (it.code != 400) return@onFailure

        account.password = null
        account.email = null
    }

}