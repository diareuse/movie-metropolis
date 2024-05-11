package movie.cinema.city

import java.util.Locale

data class CustomerModification(
    val email: String,
    val cinema: Cinema?,
    val name: Name,
    val phone: String,
    val locale: Locale,
    val consent: Consent
) {

    data class Name(
        val first: String,
        val last: String
    )

    data class Consent(
        val marketing: Boolean,
        val premium: Boolean
    )

}