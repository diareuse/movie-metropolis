package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.profile.ProfileEditorViewModel

data class UserViewFromView(
    private val origin: UserView,
    private val membership: MembershipView?,
    private val viewModel: ProfileEditorViewModel
) : UserView {

    override val firstName: String
        get() = viewModel.state.value.firstName
    override val lastName: String
        get() = viewModel.state.value.lastName
    override val email: String
        get() = viewModel.state.value.email
    override val phone: String
        get() = viewModel.state.value.phone
    override val favorite: CinemaSimpleView?
        get() = origin.favorite
    override val consent: UserView.ConsentView
        get() = Consent()

    private inner class Consent : UserView.ConsentView {

        override val marketing: Boolean
            get() = viewModel.state.value.marketingMessaging
        override val premium: Boolean
            get() = marketing && membership?.isExpired == false

    }

}