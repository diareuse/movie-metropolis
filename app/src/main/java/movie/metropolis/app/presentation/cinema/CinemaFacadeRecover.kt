package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.core.Recoverable
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

class CinemaFacadeRecover(
    private val origin: CinemaFacade
) : CinemaFacade by origin, Recoverable {

    override val cinema: Flow<Result<CinemaView>> = flow {
        try {
            origin.cinema.collect(this)
        } catch (e: Throwable) {
            emit(Result.failure(e))
        }
    }

    override fun showings(date: Date): Flow<Result<List<MovieBookingView>>> = flow {
        try {
            origin.showings(date).collect(this)
        } catch (e: Throwable) {
            emit(Result.failure(e))
        }
    }

}