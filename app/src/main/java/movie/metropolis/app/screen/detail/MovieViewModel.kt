package movie.metropolis.app.screen.detail

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import movie.core.UserDataFeature
import movie.core.model.Location
import movie.metropolis.app.model.Filter
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.cinema.BookingFilterable.Companion.optionsFlow
import movie.metropolis.app.presentation.detail.MovieFacade
import movie.metropolis.app.presentation.detail.MovieFacade.Companion.availableFromFlow
import movie.metropolis.app.presentation.detail.MovieFacade.Companion.favoriteFlow
import movie.metropolis.app.presentation.detail.MovieFacade.Companion.movieFlow
import movie.metropolis.app.presentation.detail.MovieFacade.Companion.posterFlow
import movie.metropolis.app.presentation.detail.MovieFacade.Companion.showingsFlow
import movie.metropolis.app.presentation.detail.MovieFacade.Companion.trailerFlow
import movie.metropolis.app.screen.Route
import movie.metropolis.app.util.retainStateIn
import java.util.Date
import javax.inject.Inject
import android.location.Location as AndroidLocation

@Stable
@HiltViewModel
class MovieViewModel private constructor(
    private val facade: MovieFacade,
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

    val startDate = facade.availableFromFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val detail = facade.movieFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val trailer = facade.trailerFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val poster = facade.posterFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val showings = facade.showingsFlow(selectedDate.filterNotNull(), location.filterNotNull())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())
    val options = showings.flatMapLatest { facade.optionsFlow }
        .retainStateIn(viewModelScope, Loadable.loading())
    val favorite = facade.favoriteFlow
        .retainStateIn(viewModelScope)
    val showFavorite = startDate.map { hideShowings && (it.getOrNull() ?: Date(0)) >= Date() }
        .retainStateIn(viewModelScope, false)

    init {
        viewModelScope.launch {
            facade.getAvailableFrom { result ->
                result
                    .map { if (it.before(Date())) Date() else it }
                    .onSuccess { selectedDate.compareAndSet(null, it) }
            }
        }
        viewModelScope.launch {
            var favorite: AndroidLocation? = null
            user.get { favorite = it.getOrNull()?.favorite?.location?.toPlatform() }
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