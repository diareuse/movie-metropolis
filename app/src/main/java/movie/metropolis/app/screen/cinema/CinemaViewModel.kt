package movie.metropolis.app.screen.cinema

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.model.Filter
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.cinema.BookingFilterable.Companion.optionsFlow
import movie.metropolis.app.screen.cinema.CinemaFacade.Companion.cinemaFlow
import movie.metropolis.app.screen.cinema.CinemaFacade.Companion.showingsFlow
import movie.metropolis.app.screen.retainStateIn
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CinemaViewModel private constructor(
    private val facade: CinemaFacade
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: CinemaFacade.Factory
    ) : this(
        facade.create(handle.get<String>("cinema").orEmpty())
    )

    val selectedDate = MutableStateFlow(Date())
    val cinema = facade.cinemaFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val items = facade.showingsFlow(selectedDate)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val options = items.flatMapLatest { facade.optionsFlow }
        .retainStateIn(viewModelScope)

    fun toggleFilter(filter: Filter) = facade.toggle(filter)

}

