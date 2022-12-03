package movie.metropolis.app.feature.global.adapter

import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.User
import movie.metropolis.app.feature.global.model.ConsentRemote
import movie.metropolis.app.feature.global.model.CustomerPointsResponse
import movie.metropolis.app.feature.global.model.CustomerResponse
import java.util.Date

internal data class UserFromRemote(
    private val customer: CustomerResponse.Customer,
    private val customerPoints: CustomerPointsResponse,
    override val favorite: Cinema?
) : User {

    constructor(
        customer: CustomerResponse.Customer,
        points: CustomerPointsResponse,
        cinemas: List<Cinema>
    ) : this(
        customer = customer,
        customerPoints = points,
        favorite = cinemas.firstOrNull { it.id == customer.favoriteCinema }
    )

    override val firstName: String
        get() = customer.firstName
    override val lastName: String
        get() = customer.lastName
    override val email: String
        get() = customer.email
    override val phone: String
        get() = customer.phone
    override val consent: User.Consent
        get() = ConsentFromRemote(customer.consent)
    override val membership: User.Membership?
        get() = customer.membership.club?.let(UserFromRemote::MembershipFromRemote)
    override val points: Double
        get() = customerPoints.total

    private data class ConsentFromRemote(
        private val consent: ConsentRemote
    ) : User.Consent {
        override val marketing: Boolean
            get() = consent.marketing
        override val premium: Boolean
            get() = consent.marketingPremium ?: false
    }

    private data class MembershipFromRemote(
        private val club: CustomerResponse.Club
    ) : User.Membership {
        override val cardNumber: String
            get() = club.cardNumber
        override val memberFrom: Date
            get() = club.joinedAt
        override val memberUntil: Date
            get() = club.expiredAt
    }

}