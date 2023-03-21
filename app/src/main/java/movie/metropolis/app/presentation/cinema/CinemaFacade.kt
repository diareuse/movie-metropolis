package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

interface CinemaFacade {

    val cinema: Flow<Result<CinemaView>>

    fun showings(date: Date): Flow<Result<List<MovieBookingView>>>

    interface Filterable : CinemaFacade, BookingFilterable

    fun interface Factory {
        fun create(id: String): Filterable
    }

}