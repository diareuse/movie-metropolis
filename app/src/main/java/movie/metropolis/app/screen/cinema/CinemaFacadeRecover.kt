package movie.metropolis.app.screen.cinema

import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade {

    override suspend fun getCinema(): Result<CinemaView> {
        return kotlin.runCatching { origin.getCinema().getOrThrow() }
    }

    override suspend fun getShowings(date: Date): Result<List<MovieBookingView>> {
        return kotlin.runCatching { origin.getShowings(date).getOrThrow() }
    }

}