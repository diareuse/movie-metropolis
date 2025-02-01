package movie.metropolis.app.ui.profile

import androidx.compose.runtime.*
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView
import movie.style.layout.LayoutState

@Stable
class ProfileState {

    var saving by mutableStateOf(SaveState.Idle)
    var user by mutableStateOf(LayoutState.loading<UserView>())
    val userOrNull by derivedStateOf { user.getOrNull() }
    val cinemas = mutableStateListOf<CinemaSimpleView>()
    var membership by mutableStateOf(LayoutState.loading<MembershipView>())

    enum class SaveState {
        Saving, Idle, Fail
    }

}