package movie.core.auth

data class AuthMetadata(
    val user: String,
    val password: String,
    val captcha: String
)