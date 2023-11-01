package movie.core.adapter

import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.Movie
import movie.core.model.Ticket
import java.util.Date

data class BookingFromTicket(
    private val ticket: Ticket,
    private val movie: Movie,
    override val cinema: Cinema
) : Booking {

    override val id: String
        get() = ticket.id
    override val name: String
        get() = movie.name
    override val startsAt: Date
        get() = ticket.startsAt
    override val paidAt: Date
        get() = Date()
    override val eventId: String
        get() = ticket.eventId
    override val movieId: String
        get() = ticket.movieId
    override val hall: String
        get() = ticket.venue
    override val seats: List<Booking.Seat>
        get() = ticket.seats.map(BookingFromTicket::BookingSeatFromTicket)
    override val expired: Boolean = false

    data class BookingSeatFromTicket(
        private val ticketSeat: Ticket.Seat
    ) : Booking.Seat {

        override val row: String
            get() = ticketSeat.row
        override val seat: String
            get() = ticketSeat.seat

    }

}