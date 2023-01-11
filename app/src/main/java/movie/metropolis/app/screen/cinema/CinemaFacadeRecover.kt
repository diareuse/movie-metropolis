package movie.metropolis.app.screen.cinema

import movie.log.logCatchingResult
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    override suspend fun getCinema(): Result<CinemaView> {
        return origin.logCatchingResult("cinema") { getCinema() }
    }

    override suspend fun getShowings(date: Date): Result<List<MovieBookingView>> {
        return origin.logCatchingResult("cinema-showings") { getShowings(date) }
    }

    override suspend fun getOptions(): Result<Map<Filter.Type, List<Filter>>> {
        return origin.logCatchingResult("cinema-options") { getOptions() }
    }

}