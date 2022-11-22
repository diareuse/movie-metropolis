package movie.metropolis.app.feature.user

import java.util.Date

data class User(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val birthAt: Date?,
    val favoriteCinemaId: String?,
    val consent: Consent,
    val membership: Membership?,
    val points: Double
) {

    data class Consent(
        val marketing: Boolean,
        val premium: Boolean
    )

    data class Membership(
        val cardNumber: String,
        val memberFrom: Date,
        val memberUntil: Date,
    ) {

        val isExpired
            get() = Date().after(memberUntil)

    }

}