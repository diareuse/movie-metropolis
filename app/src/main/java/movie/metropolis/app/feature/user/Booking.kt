package movie.metropolis.app.feature.user

import movie.metropolis.app.feature.global.Cinema
import java.util.Date

sealed interface Booking {

    val id: String
    val name: String
    val startsAt: Date
    val paidAt: Date
    val distributorCode: String
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