package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.cinema.BookingFilterable
import java.util.Date

interface MovieFacade : BookingFilterable {

    val movie: Flow<MovieDetailView>
    val availability: Flow<Date>

    fun showings(
        date: Date,
        latitude: Double,
        longitude: Double
    ): Flow<List<CinemaBookingView>>

    fun interface Factory {
        fun create(id: String): MovieFacade
    }

}