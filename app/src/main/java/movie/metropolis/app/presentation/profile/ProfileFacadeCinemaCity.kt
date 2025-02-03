package movie.metropolis.app.presentation.profile

import movie.cinema.city.CinemaCity
import movie.cinema.city.CustomerModification
import movie.core.auth.UserAccount
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.model.adapter.CinemaSimpleViewFromCinema
import movie.metropolis.app.model.adapter.MembershipViewFromCustomer
import movie.metropolis.app.model.adapter.UserViewFromCustomer
import java.text.DateFormat
import java.util.Locale

class ProfileFacadeCinemaCity(
    private val cinemaCity: CinemaCity,
    private val user: UserAccount
) : ProfileFacade {

    override suspend fun getCinemas(): List<CinemaSimpleView> {
        return cinemaCity.cinemas.getCinemas().map(::CinemaSimpleViewFromCinema)
    }

    override suspend fun getMembership(): MembershipView? {
        return cinemaCity.customers.runCatching { getCustomer().membership!! }
            .map { membership -> MembershipViewFromCustomer(membership, ExpirationFormat) }
            .getOrNull()
    }

    override suspend fun getUser(): UserView? {
        return cinemaCity.customers.runCatching { getCustomer() }
            .map(::UserViewFromCustomer).getOrNull()
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

    companion object {
        private val ExpirationFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    }

}