package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import movie.log.Logger
import movie.log.logSevere
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.MovieDetailView
import java.util.Date

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade by origin {

    override val movie: Flow<Result<MovieDetailView>> = flow {
        try {
            origin.movie.collect(this)
        } catch (e: Throwable) {
            Logger.error("MM", e.message ?: "Exception occurred", e)
            emit(Result.failure(e))
        }
    }
    override val favorite: Flow<Boolean> = flow {
        try {
            origin.favorite.collect(this)
        } catch (e: Throwable) {
            Logger.error("MM", e.message ?: "Exception occurred", e)
            emit(false)
        }
    }
    override val availability: Flow<Date> = flow {
        try {
            origin.availability.collect(this)
        } catch (e: Throwable) {
            Logger.error("MM", e.message ?: "Exception occurred", e)
            emit(Date())
        }
    }

    override fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<Result<List<CinemaBookingView>>> = flow {
        try {
            origin.showings(date, latitude, longitude).collect(this)
        } catch (e: Throwable) {
            emit(Result.failure(e))
        }
    }

    override suspend fun toggleFavorite() {
        origin.runCatching { toggleFavorite() }.logSevere()
    }

}