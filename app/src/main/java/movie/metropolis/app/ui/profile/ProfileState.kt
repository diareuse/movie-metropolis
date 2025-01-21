package movie.metropolis.app.ui.profile

import androidx.compose.runtime.*
import movie.metropolis.app.model.CinemaSimpleView
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.model.UserView

@Stable
class ProfileState {

    var saving by mutableStateOf(SaveState.Idle)
    var user: UserView? by mutableStateOf(null)
    val cinemas = mutableStateListOf<CinemaSimpleView>()
    var membership: MembershipView? by mutableStateOf(null)

    enum class SaveState {
        Saving, Idle, Fail
    }

}