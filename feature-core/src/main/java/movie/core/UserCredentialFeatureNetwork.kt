package movie.core

import movie.core.auth.UserAccount
import movie.core.model.SignInMethod
import movie.core.nwk.UserService
import movie.core.nwk.model.RegistrationRequest
import movie.core.nwk.model.TokenRequest

class UserCredentialFeatureNetwork(
    private val service: UserService,
    private val account: UserAccount
) : UserCredentialFeature {

    override val email: String?
        get() = account.email

    override suspend fun signIn(method: SignInMethod) = when (method) {
        is SignInMethod.Login -> signIn(method)
        is SignInMethod.Registration -> signIn(method)
    }

    override suspend fun getToken(): Result<String> = service.getCurrentToken()

    // ---

    private suspend fun signIn(method: SignInMethod.Login) =
        service.getToken(TokenRequest.Login(method.email, method.password)).map {}

    private suspend fun signIn(method: SignInMethod.Registration) =
        service.register(
            RegistrationRequest(
                email = method.email,
                firstName = method.firstName,
                lastName = method.lastName,
                password = method.password,
                phone = method.phone
            )
        ).map {}

}