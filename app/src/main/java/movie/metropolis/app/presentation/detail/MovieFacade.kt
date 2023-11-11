package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.cinema.BookingFilterable
import java.util.Date

interface MovieFacade {

    val movie: Flow<MovieDetailView>
    val favorite: Flow<Boolean>
    val availability: Flow<Date>

    fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<Result<List<CinemaBookingView>>>

    suspend fun toggleFavorite()

    interface Filterable : MovieFacade, BookingFilterable

    fun interface Factory {
        fun create(id: String): Filterable
    }

}