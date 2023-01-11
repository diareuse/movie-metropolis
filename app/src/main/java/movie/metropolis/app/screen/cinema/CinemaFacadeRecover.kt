package movie.metropolis.app.screen.cinema

import movie.log.flatMapCatching
import movie.log.log
import movie.log.logSevere
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    override suspend fun getCinema(): Result<CinemaView> {
        return origin.flatMapCatching { getCinema() }.logSevere()
    }

    override suspend fun getShowings(date: Date): Result<List<MovieBookingView>> {
        return origin.flatMapCatching { getShowings(date) }.logSevere()
    }

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> {
        return origin.flatMapCatching { getOptions() }.log()
    }

}