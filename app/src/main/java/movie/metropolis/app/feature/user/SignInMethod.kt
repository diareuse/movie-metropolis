package movie.metropolis.app.feature.user

sealed class SignInMethod {

    data class Login(
        val email: String,
        val password: String
    ) : SignInMethod()

    data class Registration(
        val email: String,
        val password: String,
        val firstName: String,
        val lastName: String,
        val phone: String
    ) : SignInMethod()

}