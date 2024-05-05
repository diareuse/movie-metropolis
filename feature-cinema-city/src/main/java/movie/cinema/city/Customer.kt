package movie.cinema.city

import java.net.URL
import java.util.Date

interface Customer {
    val id: String
    val name: Name

    interface Name {
        val first: String
        val last: String
    }

    val email: String
    val phone: String
    val birth: Date
    val cinema: Cinema?
    val image: URL
    val consent: Consent

    interface Consent {
        val marketing: Boolean
        val premium: Boolean
    }

    val membership: Membership?

    interface Membership {
        val number: String
        val inception: Date
        val expiration: Date
        val points: Points
    }

    interface Points {
        val total: Int
        val expiring: Int
        val expiringAt: Date
    }
}