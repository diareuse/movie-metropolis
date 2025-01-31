package movie.metropolis.app.ui.booking

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import movie.metropolis.app.model.FiltersView
import movie.metropolis.app.presentation.ticket.TicketFacade
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.updateWith
import javax.inject.Inject
import android.location.Location as AndroidLocation

@Stable
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
    val state = BookingScreenState()

    init {
        viewModelScope.launch {
            location.map(factory::create).collectLatest { facade ->
                coroutineScope {
                    facade.name
                        .onEach { state.title = it }
                        .launchIn(this)
                    facade.poster
                        .onEach { state.poster = it }
                        .launchIn(this)
                    facade.times
                        .onEach {
                            state.items.updateWith(it)
                        }
                        .launchIn(this)
                    state.filters = facade.filters
                }
            }
        }
    }

    fun toggle(filter: FiltersView.Type) {
        state.filters.types.forEach {
            if (it.type == filter.type) it.selected = !it.selected
        }
    }

    fun toggle(filter: FiltersView.Language) {
        state.filters.languages.forEach {
            if (it.locale == filter.locale) it.selected = !it.selected
        }
    }

}