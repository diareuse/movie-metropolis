package movie.metropolis.app.screen.cinema

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.feature.global.EventFeature
import movie.metropolis.app.feature.global.Location
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.screen.cinema.CinemasFacade.Companion.cinemasFlow
import javax.inject.Inject

@HiltViewModel
class CinemasViewModel @Inject constructor(
    facade: CinemasFacade
) : ViewModel() {

    val location = MutableStateFlow(null as android.location.Location?)
    val items = facade.cinemasFlow(location)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), Loadable.loading())

}

interface CinemasFacade {

    suspend fun getCinemas(latitude: Double?, longitude: Double?): Result<List<CinemaView>>

    companion object {

        fun CinemasFacade.cinemasFlow(
            location: Flow<android.location.Location?>
        ) = location.flatMapLatest {
            flow {
                emit(Loadable.loading())
                emit(getCinemas(it?.latitude, it?.longitude).asLoadable())
            }
        }

    }

}

class CinemasFacadeFromFeature(
    private val event: EventFeature
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ): Result<List<CinemaView>> {
        if (latitude == null || longitude == null)
            return event.getCinemas(null).map { it.map(::CinemaViewFromFeature) }

        val location = Location(latitude, longitude)
        return event.getCinemas(location).map { it.map(::CinemaViewFromFeature) }
    }

}

class CinemasFacadeRecover(
    private val origin: CinemasFacade
) : CinemasFacade {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?
    ) = kotlin.runCatching { origin.getCinemas(latitude, longitude).getOrThrow() }

}