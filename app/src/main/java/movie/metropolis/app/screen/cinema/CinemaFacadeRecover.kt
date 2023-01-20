package movie.metropolis.app.screen.cinema

import movie.core.Recoverable
import movie.core.ResultCallback
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
        runCatchingResult(callback) {
            origin.getCinema { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getShowings(date: Date, callback: ResultCallback<List<MovieBookingView>>) {
        runCatchingResult(callback) {
            origin.getShowings(date) { result ->
                it(result.logSevere())
            }
        }
    }

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> {
        return origin.flatMapCatching { getOptions() }.log()
    }

}