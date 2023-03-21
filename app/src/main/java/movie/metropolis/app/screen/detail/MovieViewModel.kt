package movie.metropolis.app.screen.detail

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import movie.core.UserDataFeature
import movie.core.model.Location
import movie.metropolis.app.model.Filter
import movie.metropolis.app.presentation.asLoadable
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.presentation.mapLoadable
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import java.util.Date
import javax.inject.Inject
import android.location.Location as AndroidLocation

@Stable
@HiltViewModel
class MovieViewModel private constructor(
    private val facade: MovieFacade.Filterable,
    private val user: UserDataFeature,
    val hideShowings: Boolean
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: MovieFacade.Factory,
        user: UserDataFeature
    ) : this(
        Route.Movie.Arguments(handle),
        facade,
        user
    )

    constructor(
        args: Route.Movie.Arguments,
        facade: MovieFacade.Factory,
        user: UserDataFeature
    ) : this(
        facade.create(args.movie),
        user,
        args.upcoming
    )

    val selectedDate = MutableStateFlow(null as Date?)
    val location = MutableStateFlow(null as AndroidLocation?)

    val detail = facade.movie
        .map { it.asLoadable() }
        .retainStateIn(viewModelScope)
    val startDate = facade.availability
        .onEach { selectedDate.compareAndSet(null, it) }
        .retainStateIn(viewModelScope, Date(0))
    val trailer = detail.mapLoadable { it.trailer }
        .retainStateIn(viewModelScope)
    val poster = detail.mapLoadable { it.poster }
        .retainStateIn(viewModelScope)
    val showings = combineTransform(
        selectedDate.filterNotNull(),
        location.filterNotNull()
    ) { date, location ->
        facade.showings(date, location.latitude, location.longitude)
            .map { it.asLoadable() }
            .collect(this)
    }.retainStateIn(viewModelScope)
    val options = facade.options
        .retainStateIn(viewModelScope, emptyMap())
    val favorite = facade.favorite
        .retainStateIn(viewModelScope, false)
    val showFavorite = startDate.map { hideShowings && it >= Date() }
        .retainStateIn(viewModelScope, false)

    init {
        viewModelScope.launch {
            val favorite: AndroidLocation? = user.get()
                .map { it.favorite?.location?.toPlatform() }
                .getOrNull()
            location.compareAndSet(null, favorite)
        }
    }

    fun toggleFilter(filter: Filter) = facade.toggle(filter)
    fun toggleFavorite() = viewModelScope.launch {
        facade.toggleFavorite()
    }

    private fun Location.toPlatform() = AndroidLocation(null).also {
        it.latitude = latitude
        it.longitude = longitude
    }

}