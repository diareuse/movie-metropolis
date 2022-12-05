package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.profile.ProfileViewModel

data class UserViewFromView(
    private val viewModel: ProfileViewModel
) : UserView {

    override val firstName: String
        get() = viewModel.firstName.value
    override val lastName: String
        get() = viewModel.lastName.value
    override val email: String
        get() = viewModel.email.value
    override val phone: String
        get() = viewModel.phone.value
    override val favorite: CinemaSimpleView?
        get() = viewModel.favorite.value
    override val consent: UserView.ConsentView
        get() = Consent()

    private inner class Consent : UserView.ConsentView {

        override val marketing: Boolean
            get() = viewModel.hasMarketing.value == true
        override val premium: Boolean
            get() = marketing && viewModel.membership.value.getOrNull()?.isExpired == false

    }

}