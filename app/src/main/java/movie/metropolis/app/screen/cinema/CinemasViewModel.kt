package movie.metropolis.app.screen.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.cinema.CinemasFacade.Companion.cinemasFlow
import javax.inject.Inject

@HiltViewModel
class CinemasViewModel @Inject constructor(
    facade: CinemasFacade
) : ViewModel() {

    val location = MutableStateFlow(null as android.location.Location?)
    val items = facade.cinemasFlow(location)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}

