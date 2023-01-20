package movie.metropolis.app.screen.cinema

import movie.core.ResultCallback
import movie.metropolis.app.model.CinemaView

class CinemaFacadeCaching(
    private val origin: CinemaFacade
) : CinemaFacade by origin {

    private var cinema: CinemaView? = null

    override suspend fun getCinema(callback: ResultCallback<CinemaView>) {
        val cinema = cinema
        if (cinema != null)
            return callback(Result.success(cinema))
        origin.getCinema {
            callback(it)
            it.onSuccess { cinema ->
                this.cinema = cinema
            }
        }
    }

}