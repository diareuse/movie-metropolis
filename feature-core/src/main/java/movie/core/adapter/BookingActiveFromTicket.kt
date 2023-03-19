package movie.core.adapter

import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.Movie
import movie.core.model.Ticket
import java.util.Date

data class BookingActiveFromTicket(
    private val ticket: Ticket,
    private val movie: Movie,
    override val cinema: Cinema
) : Booking.Active {

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
    override val seats: List<Booking.Active.Seat>
        get() = ticket.seats.map(BookingActiveFromTicket::BookingSeatFromTicket)

    data class BookingSeatFromTicket(
        private val ticketSeat: Ticket.Seat
    ) : Booking.Active.Seat {

        override val row: String
            get() = ticketSeat.row
        override val seat: String
            get() = ticketSeat.seat

    }

}