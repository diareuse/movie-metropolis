package movie.core.nwk

import movie.core.auth.UserAccount
import movie.core.auth.UserCredentials
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest

internal class UserServiceSaving(
    private val origin: UserService,
    private val credentials: UserCredentials,
    private val account: UserAccount
) : UserService by origin {

    override suspend fun register(
        request: RegistrationRequest
    ) = origin.register(request).onSuccess {
        credentials.email = request.email
        credentials.password = request.password
        account.token = it.token?.accessToken
        account.refreshToken = it.token?.refreshToken
        account.expirationDate = it.token?.expiresAt
    }

    override suspend fun getToken(
        request: TokenRequest
    ) = origin.getToken(request).onSuccess {
        if (request is TokenRequest.Login) {
            credentials.email = request.username
            credentials.password = request.password
        }
        account.token = it.accessToken
        account.refreshToken = it.refreshToken
        account.expirationDate = it.expiresAt
    }

    override suspend fun updatePassword(
        request: PasswordRequest
    ) = origin.updatePassword(request).onSuccess {
        credentials.password = request.new
    }

    override suspend fun updateUser(
        request: CustomerDataRequest
    ) = origin.updateUser(request).onSuccess {
        credentials.email = it.customer.email
    }

}

