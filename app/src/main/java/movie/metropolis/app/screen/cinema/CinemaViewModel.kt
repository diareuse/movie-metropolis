package movie.metropolis.app.screen.cinema

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import movie.metropolis.app.model.Filter
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.asLoadable
import movie.metropolis.app.presentation.cinema.CinemaFacade
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import java.util.Date
import javax.inject.Inject

@Stable
@HiltViewModel
class CinemaViewModel private constructor(
    private val facade: CinemaFacade.Filterable
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: CinemaFacade.Factory
    ) : this(
        facade.create(Route.Cinema.Arguments(handle).cinema)
    )

    val selectedDate = MutableStateFlow(Date())
    val cinema = facade.cinema
        .map { it.asLoadable() }
        .retainStateIn(viewModelScope)
    val items = selectedDate
        .flatMapLatest {
            flow {
                emit(Loadable.loading())
                facade.showings(it).map { it.asLoadable() }.collect(this)
            }
        }
        .retainStateIn(viewModelScope)
    val options = facade.options
        .retainStateIn(viewModelScope, emptyMap())

    fun toggleFilter(filter: Filter) = facade.toggle(filter)

}

