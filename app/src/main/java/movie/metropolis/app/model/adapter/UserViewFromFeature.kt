package movie.metropolis.app.model.adapter

import movie.core.model.User
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.UserView

data class UserViewFromFeature(
    private val user: User
) : UserView {

    override val firstName: String
        get() = user.firstName
    override val lastName: String
        get() = user.lastName
    override val email: String
        get() = user.email
    override val phone: String
        get() = user.phone
    override val favorite: CinemaSimpleView?
        get() = user.favorite?.let(::CinemaSimpleViewFromFeature)
    override val consent: UserView.ConsentView
        get() = ConsentViewFromFeature(user.consent)

    private data class ConsentViewFromFeature(
        private val consent: User.Consent
    ) : UserView.ConsentView {

        override val marketing: Boolean
            get() = consent.marketing
        override val premium: Boolean
            get() = consent.premium

    }

}