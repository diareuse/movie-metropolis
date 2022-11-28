package movie.metropolis.app.feature.user

import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.MovieDetail
import java.util.Date

sealed interface Booking {

    val id: String
    val name: String
    val startsAt: Date
    val paidAt: Date
    val movie: MovieDetail
    val eventId: String
    val cinema: Cinema

    interface Expired : Booking

    interface Active : Booking {

        val hall: String
        val seats: List<Seat>

        interface Seat {
            val row: String
            val seat: String
        }

    }

}