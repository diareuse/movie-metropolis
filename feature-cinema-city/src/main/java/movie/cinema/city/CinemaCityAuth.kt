package movie.cinema.city

internal object CinemaCityAuth {
    val user inline get() = BuildConfig.BasicUser
    val pass inline get() = BuildConfig.BasicPass
    val captcha inline get() = BuildConfig.Captcha
}