package movie.metropolis.app.feature.global

import java.util.Date

interface User {

    val firstName: String
    val lastName: String
    val email: String
    val phone: String
    val favorite: Cinema?
    val consent: Consent
    val membership: Membership?
    val points: Double

    interface Consent {
        val marketing: Boolean
        val premium: Boolean
    }

    interface Membership {
        val cardNumber: String
        val memberFrom: Date
        val memberUntil: Date
        val isExpired
            get() = Date().after(memberUntil)
    }

}
