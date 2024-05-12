package movie.cinema.city.adapter

import movie.cinema.city.Cinema
import movie.cinema.city.Movie
import movie.cinema.city.Ticket
import movie.cinema.city.persistence.TicketStored
import java.util.Date

internal data class TicketFromDatabase(
    private val ticket: TicketStored,
    private val ticketReservations: List<TicketStored.Reservation>,
    override val cinema: Cinema,
    override val movie: Movie
) : Ticket {
    override val id: String
        get() = ticket.id
    override val name: String
        get() = ticket.name
    override val startsAt: Date
        get() = ticket.startsAt
    override val paidAt: Date?
        get() = ticket.paidAt
    override val venue: Ticket.Venue = Venue()

    private inner class Venue : Ticket.Venue {
        override val name: String
            get() = ticket.venue
        override val reservations: List<Ticket.Reservation>
            get() = ticketReservations.map(::Reservation)
    }

    private inner class Reservation(
        private val reservation: TicketStored.Reservation
    ) : Ticket.Reservation {
        override val row: String
            get() = reservation.row
        override val seat: String
            get() = reservation.seat
    }
}