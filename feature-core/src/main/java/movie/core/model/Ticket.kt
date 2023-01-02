package movie.core.model

import java.util.Date

interface Ticket {

    val id: String
    val startsAt: Date
    val venue: String
    val movieId: String
    val eventId: String
    val cinemaId: String
    val seats: List<Seat>

    interface Seat {
        val row: String
        val seat: String
    }

}