package movie.metropolis.app.presentation.login

import movie.core.PosterFeature
import movie.core.SetupFeature
import movie.core.UserCredentialFeature
import movie.core.model.SignInMethod

class LoginFacadeFromFeature(
    private val user: UserCredentialFeature,
    private val setup: SetupFeature,
    private val poster: PosterFeature
) : LoginFacade {

    override val currentUserEmail get() = user.email
    override val domain: String
        get() = setup.region.domain

    override suspend fun getPosters(): List<String> {
        return poster.get()
    }

    override suspend fun login(
        email: String,
        password: String
    ): Result<Unit> {
        val method = SignInMethod.Login(email, password)
        return user.signIn(method)
    }

    override suspend fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        phone: String
    ): Result<Unit> {
        val method = SignInMethod.Registration(email, password, firstName, lastName, phone)
        return user.signIn(method)
    }

}