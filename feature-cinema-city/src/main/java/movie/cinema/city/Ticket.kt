package movie.cinema.city

import java.util.Date

interface Ticket {
    val id: String
    val name: String
    val startsAt: Date
    val paidAt: Date?
    val movie: Movie
    val cinema: Cinema
    val venue: Venue

    interface Venue {
        val name: String
        val reservations: List<Reservation>
    }

    interface Reservation {
        val row: String
        val seat: String
    }
}