package movie.metropolis.app.screen2.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.home.HomeFacade
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.presentation.profile.ProfileFacade.Companion.membershipFlow
import movie.metropolis.app.presentation.profile.ProfileFacade.Companion.userFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    profile: ProfileFacade,
    private val home: HomeFacade
) : ViewModel() {

    val isLoggedIn get() = home.email != null
    val user = profile.userFlow(emptyFlow()).map { it.getOrNull() }
        .filterNot { it?.email.isNullOrBlank() }
        .retainStateIn(viewModelScope, null)
    val membership = profile.membershipFlow.map { it.getOrNull() }
        .retainStateIn(viewModelScope, null)

}