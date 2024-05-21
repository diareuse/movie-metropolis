package movie.metropolis.app.model.adapter

import movie.cinema.city.Customer
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.UserView

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