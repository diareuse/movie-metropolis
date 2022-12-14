package movie.metropolis.app.screen.profile

import movie.core.UserFeature
import movie.core.model.SignInMethod

class LoginFacadeFromFeature(
    private val user: UserFeature,
) : LoginFacade {

    override val currentUserEmail get() = user.email

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