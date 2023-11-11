package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import movie.log.logSevere
import movie.metropolis.app.model.CinemaBookingView
import java.util.Date

class MovieFacadeRecover(
    private val origin: MovieFacade
) : MovieFacade by origin {

    override val favorite: Flow<Boolean> = origin.favorite
        .catch { emit(false) }
    override val availability: Flow<Date> = origin.availability
        .catch { emit(Date()) }

    override fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<Result<List<CinemaBookingView>>> = origin.showings(date, latitude, longitude)
        .catch { emit(Result.failure(it)) }

    override suspend fun toggleFavorite() {
        origin.runCatching { toggleFavorite() }.logSevere()
    }

}