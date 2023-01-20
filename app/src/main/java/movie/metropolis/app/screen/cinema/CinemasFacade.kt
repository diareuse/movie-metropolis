package movie.metropolis.app.screen.cinema

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.asLoadable

interface CinemasFacade {

    suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?,
        callback: ResultCallback<List<CinemaView>>
    )

    companion object {

        fun CinemasFacade.cinemasFlow(
            location: Flow<Location?>
        ) = location.flatMapLatest {
            flow {
                getCinemas(it?.latitude, it?.longitude) {
                    emit(it.asLoadable())
                }
            }
        }

    }

}