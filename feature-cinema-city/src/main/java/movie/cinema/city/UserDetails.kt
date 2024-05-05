package movie.cinema.city

data class UserDetails(
    val email: String,
    val firstName: String,
    val lastName: String,
    val password: String,
    val phone: String,
    val marketing: Boolean,
    val premium: Boolean
)