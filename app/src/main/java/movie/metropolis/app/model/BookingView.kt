package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
sealed interface BookingView {

    val id: String
    val name: String
    val date: String
    val time: String
    val isPaid: Boolean
    val movie: MovieDetailView
    val cinema: CinemaView

    @Immutable
    interface Expired : BookingView

    @Immutable
    interface Active : BookingView {

        val hall: String
        val seats: List<Seat>

        @Immutable
        interface Seat {
            val row: String
            val seat: String
        }

    }

}