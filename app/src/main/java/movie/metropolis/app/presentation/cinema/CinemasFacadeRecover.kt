package movie.metropolis.app.presentation.cinema

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.core.result
import movie.log.logSevere
import movie.metropolis.app.model.CinemaView

class CinemasFacadeRecover(
    private val origin: CinemasFacade
) : CinemasFacade, Recoverable {

    override suspend fun getCinemas(
        latitude: Double?,
        longitude: Double?,
        callback: ResultCallback<List<CinemaView>>
    ) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getCinemas(latitude, longitude, it)
        }
    }

}