package movie.metropolis.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.presentation.profile.ProfileFacade
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val facade: ProfileFacade
) : ViewModel() {

    val state = ProfileState()

    init {
        viewModelScope.launch {
            launch {
                state.user = facade.getUser()
            }
            launch {
                state.cinemas.clear()
                state.cinemas.addAll(facade.getCinemas())
            }
            launch {
                state.membership = facade.getMembership()
            }
        }
    }

}