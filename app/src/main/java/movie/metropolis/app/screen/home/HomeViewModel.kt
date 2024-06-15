package movie.metropolis.app.screen.home

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.presentation.home.HomeFacade
import movie.metropolis.app.presentation.profile.ProfileFacade
import movie.metropolis.app.util.retainStateIn
import movie.metropolis.app.util.retryOnNetworkError
import javax.inject.Inject

@Stable
@HiltViewModel
class HomeViewModel @Inject constructor(
    profile: ProfileFacade,
    private val home: HomeFacade
) : ViewModel() {

    val isLoggedIn get() = home.email != null
    val user = flow { emit(profile.getUser()) }
        .filterNot { it?.email.isNullOrBlank() }
        .retryOnNetworkError()
        .retainStateIn(viewModelScope, null)
    val membership = flow { emit(profile.getMembership()) }
        .retryOnNetworkError()
        .retainStateIn(viewModelScope, null)

}