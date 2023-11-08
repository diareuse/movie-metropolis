package movie.metropolis.app.screen2.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import movie.core.model.Location
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.presentation.ticket.TicketFacade
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject
import android.location.Location as AndroidLocation

@HiltViewModel
class TimeViewModel private constructor(
    private val factory: TicketFacade.LocationFactory
) : ViewModel() {

    @Inject
    constructor(
        state: SavedStateHandle,
        factory: TicketFacade.Factory
    ) : this(
        when {
            state.contains("cinema") -> factory.cinema(state["cinema"]!!)
            state.contains("movie") -> factory.movie(state["movie"]!!)
            else -> error("Cannot find 'cinema' or 'movie' parameters")
        }
    )

    val location = MutableStateFlow(null as AndroidLocation?)
    private val facade = location
        .map { it?.toLocation() ?: Location.Zero }
        .map { factory.create(it) }
        .shareIn(viewModelScope, SharingStarted.WhileSubscribed(), 1)
    val poster = facade
        .flatMapLatest { it.poster }
        .retainStateIn(viewModelScope, null)
    val title = facade
        .flatMapLatest { it.name }
        .retainStateIn(viewModelScope, "")
    val times = facade
        .flatMapLatest { it.times }
        .retainStateIn(viewModelScope, emptyList())
    val filters = facade
        .flatMapLatest { it.filters }
        .retainStateIn(viewModelScope, DataFiltersView())

}

fun AndroidLocation.toLocation() = Location(latitude, longitude)