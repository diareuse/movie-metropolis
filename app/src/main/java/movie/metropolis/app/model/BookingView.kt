package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
sealed interface BookingView {

    val id: String
    val name: String
    val date: String
    val time: String
    val isExpired: Boolean
    val isPaid: Boolean
    val cinema: CinemaView

    @Stable
    interface Expired : BookingView

    @Stable
    interface Active : BookingView {

        val hall: String
        val seats: List<Seat>

        @Stable
        interface Seat {
            val row: String
            val seat: String
        }

    }

}