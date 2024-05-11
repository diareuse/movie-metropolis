package movie.metropolis.app.presentation.cinema

import androidx.compose.runtime.*
import kotlinx.coroutines.flow.Flow
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.MovieBookingView
import java.util.Date

@Stable
interface CinemaFacade : BookingFilterable {

    val cinema: Flow<CinemaView>

    fun showings(date: Date): Flow<List<MovieBookingView>>

    fun interface Factory {
        fun create(id: String): CinemaFacade
    }

}