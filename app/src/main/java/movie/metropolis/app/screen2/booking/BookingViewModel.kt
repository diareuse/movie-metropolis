package movie.metropolis.app.screen2.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import movie.core.model.Location
import movie.metropolis.app.feature.location.toLocation
import movie.metropolis.app.model.DataFiltersView
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.presentation.ticket.TicketFacade
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject
import android.location.Location as AndroidLocation

@HiltViewModel
class BookingViewModel private constructor(
    private val factory: TicketFacade.LocationFactory
) : ViewModel() {

    @Inject
    constructor(
        state: SavedStateHandle,
        factory: TicketFacade.Factory
    ) : this(
        when {
            Route.Booking.Movie.Arguments.keys.all { it in state } ->
                factory.movie(Route.Booking.Movie.Arguments(state).movie)

            Route.Booking.Cinema.Arguments.keys.all { it in state } ->
                factory.cinema(Route.Booking.Cinema.Arguments(state).cinema)

            else -> error("Cannot find 'cinema' or 'movie' parameters")
        }
    )

    val location = MutableStateFlow(null as AndroidLocation?)
    private val facade = location
        .map { it?.toLocation() ?: Location.Zero }
        .map { factory.create(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)
    val poster = facade
        .filterNotNull()
        .flatMapLatest { it.poster }
        .retainStateIn(viewModelScope, null)
    val title = facade
        .filterNotNull()
        .flatMapLatest { it.name }
        .retainStateIn(viewModelScope, "")
    val times = facade
        .filterNotNull()
        .flatMapLatest { it.times }
        .map { it.toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())
    val filters = facade
        .filterNotNull()
        .flatMapLatest { it.filters }
        .retainStateIn(viewModelScope, DataFiltersView())

    fun toggle(filter: FiltersView.Type) {
        facade.value?.toggle(filter)
    }

    fun toggle(filter: FiltersView.Language) {
        facade.value?.toggle(filter)
    }

}