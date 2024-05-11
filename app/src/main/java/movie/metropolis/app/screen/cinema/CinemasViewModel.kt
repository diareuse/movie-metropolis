package movie.metropolis.app.screen.cinema

import android.location.Location
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import movie.metropolis.app.presentation.cinema.CinemasFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@Stable
@HiltViewModel
class CinemasViewModel @Inject constructor(
    facade: CinemasFacade
) : ViewModel() {

    val location = MutableStateFlow(null as Location?)

    val cinemas = location.flatMapLatest { facade.cinemas(it) }
        .map { it.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

}