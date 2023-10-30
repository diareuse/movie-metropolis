package movie.metropolis.app.screen2.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.presentation.profile.ProfileFacade.Companion.userFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    profile: ProfileFacade
) : ViewModel() {

    val user = profile.userFlow(emptyFlow())

}