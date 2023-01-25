package movie.metropolis.app.screen.cinema

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.asLoadable
import movie.metropolis.app.util.throttleWithTimeout
import kotlin.time.Duration.Companion.seconds

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
        }.throttleWithTimeout(1.seconds)

    }

}