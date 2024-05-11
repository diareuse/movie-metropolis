package movie.metropolis.app.presentation.profile

import movie.cinema.city.Cinema
import movie.cinema.city.CinemaCity
import movie.cinema.city.Customer
import movie.cinema.city.CustomerModification
import movie.core.auth.UserAccount
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit

class ProfileFacadeCinemaCity(
    private val cinemaCity: CinemaCity,
    private val user: UserAccount
) : ProfileFacade {

    override suspend fun getCinemas(): List<CinemaSimpleView> {
        return cinemaCity.cinemas.getCinemas().map(::CinemaSimpleViewFromCinema)
    }

    override suspend fun getMembership(): MembershipView? {
        return cinemaCity.customers.getCustomer().membership?.let(::MembershipViewFromCustomer)
    }

    override suspend fun getUser(): UserView {
        return cinemaCity.customers.getCustomer().let(::UserViewFromCustomer)
    }

    override suspend fun isLoggedIn(): Boolean {
        return user.isLoggedIn
    }

    override suspend fun save(view: UserView) {
        val favorite = view.favorite
        check(favorite is CinemaSimpleViewFromCinema)
        val modification = CustomerModification(
            email = view.email,
            cinema = favorite.cinema,
            name = CustomerModification.Name(view.firstName, view.lastName),
            phone = view.phone,
            locale = Locale.getDefault(),
            consent = CustomerModification.Consent(view.consent.marketing, view.consent.premium)
        )
        cinemaCity.customers.updateCustomer(modification)
    }

    override suspend fun save(passwordOld: String, passwordNew: String) {
        try {
            cinemaCity.customers.updatePassword(passwordOld, passwordNew)
        } finally {
            user.password = passwordNew
        }
    }

}

data class UserViewFromCustomer(
    private val customer: Customer,
) : UserView {
    override val firstName: String
        get() = customer.name.first
    override val lastName: String
        get() = customer.name.last
    override val email: String
        get() = customer.email
    override val phone: String
        get() = customer.phone
    override val favorite: CinemaSimpleView?
        get() = customer.cinema?.let(::CinemaSimpleViewFromCinema)
    override val consent: UserView.ConsentView = Consent()

    private inner class Consent : UserView.ConsentView {
        override val marketing: Boolean
            get() = customer.consent.marketing
        override val premium: Boolean
            get() = customer.consent.premium
    }
}

data class MembershipViewFromCustomer(
    private val membership: Customer.Membership
) : MembershipView {
    override val isExpired: Boolean
        get() = membership.expiration.before(Date())
    override val cardNumber: String
        get() = membership.number
    override val memberFrom: String
        get() = membership.inception.toString()
    override val memberUntil: String
        get() = membership.expiration.toString()
    override val daysRemaining: String
        get() = (membership.expiration.time - Date().time).days.toString(DurationUnit.DAYS)
    override val points: String
        get() = membership.points.total.toString()
}

data class CinemaSimpleViewFromCinema(
    val cinema: Cinema
) : CinemaSimpleView {

    override val id: String
        get() = cinema.id
    override val name: String
        get() = cinema.name
    override val city: String
        get() = cinema.address.city

}