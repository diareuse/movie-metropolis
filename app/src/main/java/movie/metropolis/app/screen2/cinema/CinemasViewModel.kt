package movie.metropolis.app.screen2.cinema

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.presentation.cinema.CinemasFacade.Companion.cinemasFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class CinemasViewModel @Inject constructor(
    facade: CinemasFacade
) : ViewModel() {

    val location = MutableStateFlow(null as Location?)

    val cinemas = facade.cinemasFlow(location)
        .map { it.getOrNull().orEmpty() }
        .retainStateIn(viewModelScope, emptyList())

}