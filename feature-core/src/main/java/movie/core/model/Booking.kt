package movie.core.model

import java.util.Date

interface Booking {

    val id: String
    val name: String
    val startsAt: Date
    val paidAt: Date
    val movieId: String
    val eventId: String
    val cinema: Cinema
    val expired: Boolean

    val hall: String
    val seats: List<Seat>

    interface Seat {
        val row: String
        val seat: String
    }

}