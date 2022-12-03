package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.feature.global.model.Location
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.MovieFacade.Companion.availableFromFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.movieFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.posterFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.showingsFlow
import movie.metropolis.app.screen.detail.MovieFacade.Companion.trailerFlow
import java.util.Date
import javax.inject.Inject
import android.location.Location as AndroidLocation

@HiltViewModel
class MovieViewModel private constructor(
    private val facade: MovieFacade,
    private val user: UserFeature
) : ViewModel() {

    @Inject
    constructor(
        handle: SavedStateHandle,
        facade: MovieFacade.Factory,
        user: UserFeature
    ) : this(
        facade.create(handle.get<String>("movie").orEmpty()),
        user
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

    init {
        viewModelScope.launch {
            facade.getAvailableFrom()
                .map { if (it.before(Date())) Date() else it }
                .onSuccess { selectedDate.compareAndSet(null, it) }
        }
        viewModelScope.launch {
            user.getUser()
                .map { it.favorite?.location?.toPlatform() }
                .onSuccess { location.compareAndSet(null, it) }
        }
    }

    private fun Location.toPlatform() = AndroidLocation(null).also {
        it.latitude = latitude
        it.longitude = longitude
    }

}