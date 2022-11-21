package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import movie.metropolis.app.feature.user.User
import movie.metropolis.app.model.MembershipView
import movie.metropolis.app.screen.Loadable
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val user: Flow<Loadable<User>> = TODO()
    val membership: Flow<Loadable<MembershipView?>> = TODO()

    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val email = MutableStateFlow("")
    val phone = MutableStateFlow("")
    val birthDate = MutableStateFlow(null as Date?)
    val favorite = MutableStateFlow(null as String?)
    val hasMarketing = MutableStateFlow(null as Boolean?)
    val passwordCurrent = MutableStateFlow("")
    val passwordNew = MutableStateFlow("")

    fun save() {
        TODO()
    }

}