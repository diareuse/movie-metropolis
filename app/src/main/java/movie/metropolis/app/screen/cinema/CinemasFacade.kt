package movie.metropolis.app.screen.cinema

import android.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.asLoadable

interface CinemasFacade {

    suspend fun getCinemas(latitude: Double?, longitude: Double?): Result<List<CinemaView>>

    companion object {

        fun CinemasFacade.cinemasFlow(
            location: Flow<Location?>
        ) = location.flatMapLatest {
            flow {
                emit(Loadable.loading())
                emit(getCinemas(it?.latitude, it?.longitude).asLoadable())
            }
        }

    }

}