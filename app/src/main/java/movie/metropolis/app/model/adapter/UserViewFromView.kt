package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.metropolis.app.screen.profile.ProfileEditorViewModel

fun UserViewFromView(
    origin: UserView,
    membership: MembershipView?,
    viewModel: ProfileEditorViewModel
) = UserView(viewModel.state.value.email).apply {
    firstName = viewModel.state.value.firstName
    lastName = viewModel.state.value.lastName
    phone = viewModel.state.value.phone
    favorite = origin.favorite
    consent.marketing = viewModel.state.value.marketingMessaging
    consent.premium = consent.marketing && membership?.isExpired == false
}