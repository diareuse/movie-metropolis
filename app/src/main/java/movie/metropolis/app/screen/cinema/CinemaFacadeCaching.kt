package movie.metropolis.app.screen.cinema

import movie.metropolis.app.model.CinemaView

class CinemaFacadeCaching(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    private var cinema: CinemaView? = null

    override suspend fun getCinema() = cinema?.let(Result.Companion::success)
        ?: origin.getCinema().onSuccess {
            cinema = it
        }

}