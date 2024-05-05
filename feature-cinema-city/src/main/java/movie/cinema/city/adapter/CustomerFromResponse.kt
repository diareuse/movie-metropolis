package movie.cinema.city.adapter

import movie.cinema.city.Cinema
import movie.cinema.city.Customer
import movie.cinema.city.model.CustomerPointsResponse
import movie.cinema.city.model.CustomerResponse
import java.net.URL
import java.util.Date

internal data class CustomerFromResponse(
    private val customer: CustomerResponse.Customer,
    private val points: CustomerPointsResponse,
    override val cinema: Cinema?,
    override val image: URL
) : Customer {
    override val id: String
        get() = customer.id
    override val name: Customer.Name = Name()
    override val email: String
        get() = customer.email
    override val phone: String
        get() = customer.phone
    override val birth: Date
        get() = customer.birthAt ?: Date(0)
    override val consent: Customer.Consent = Consent()
    override val membership: Customer.Membership?
        get() = customer.membership.club?.let(::Membership)

    private inner class Consent : Customer.Consent {
        override val marketing: Boolean
            get() = customer.consent.marketing
        override val premium: Boolean
            get() = customer.consent.marketingPremium == true
    }

    private inner class Name : Customer.Name {
        override val first: String
            get() = customer.firstName
        override val last: String
            get() = customer.lastName
    }

    private inner class Membership(private val club: CustomerResponse.Club) : Customer.Membership {
        override val number: String
            get() = club.cardNumber
        override val inception: Date
            get() = club.joinedAt
        override val expiration: Date
            get() = club.expiredAt
        override val points: Customer.Points = Points()
    }

    private inner class Points : Customer.Points {
        override val total: Int
            get() = points.total.toInt()
        override val expiring: Int
            get() = points.expire.toInt()
        override val expiringAt: Date
            get() = points.expiresAt
    }
}