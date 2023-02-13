package movie.metropolis.app.presentation.cinema

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.core.result
import movie.log.flatMapCatching
import movie.log.log
import movie.log.logSevere
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade by origin, Recoverable {

    override suspend fun getCinema(callback: ResultCallback<CinemaView>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getCinema(it)
        }
    }

    override suspend fun getShowings(date: Date, callback: ResultCallback<List<MovieBookingView>>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getShowings(date, it)
        }
    }

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> {
        return origin.flatMapCatching { getOptions() }.log()
    }

}