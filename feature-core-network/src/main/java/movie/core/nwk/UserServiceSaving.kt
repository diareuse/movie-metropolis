package movie.core.nwk

import movie.core.auth.UserAccount
import movie.core.nwk.model.CustomerDataRequest
import movie.core.nwk.model.PasswordRequest
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest

internal class UserServiceSaving(
    private val origin: UserService,
    private val account: UserAccount
) : UserService by origin {

    override suspend fun register(
        request: RegistrationRequest
    ) = origin.register(request).onSuccess {
        account.email = request.email
        account.password = request.password
        account.token = it.token?.accessToken
        account.refreshToken = it.token?.refreshToken
        account.expirationDate = it.token?.expiresAt
    }

    override suspend fun getToken(
        request: TokenRequest
    ) = origin.getToken(request).onSuccess {
        if (request is TokenRequest.Login) {
            account.email = request.username
            account.password = request.password
        }
        account.token = it.accessToken
        account.refreshToken = it.refreshToken
        account.expirationDate = it.expiresAt
    }

    override suspend fun updatePassword(
        request: PasswordRequest
    ) = origin.updatePassword(request).onSuccess {
        account.password = request.new
    }

    override suspend fun updateUser(
        request: CustomerDataRequest
    ) = origin.updateUser(request).onSuccess {
        account.email = it.customer.email
    }

}

