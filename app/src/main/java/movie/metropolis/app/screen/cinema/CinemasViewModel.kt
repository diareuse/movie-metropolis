package movie.metropolis.app.screen.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Location
import movie.metropolis.app.model.LocationSnapshot
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.map
import javax.inject.Inject

@HiltViewModel
class CinemasViewModel @Inject constructor(
    private val event: EventFeature
) : ViewModel() {

    val location = MutableStateFlow(null as LocationSnapshot?)
    val items = location
        .map { snap -> snap?.let { Location(it.latitude, it.longitude) } }
        .map { event.getCinemas(it).asLoadable().map { it.map(::CinemaViewFromFeature) } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}