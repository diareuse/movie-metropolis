package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface BookingView {

    val id: String
    val name: String
    val date: String
    val time: String
    val isPaid: Boolean
    val movie: MovieDetailView
    val cinema: CinemaView
    val expired: Boolean

    val hall: String
    val seats: List<Seat>

    fun origin(): BookingView

    @Immutable
    interface Seat {
        val row: String
        val seat: String
    }

}