package movie.cinema.city.adapter

import movie.cinema.city.Cinema
import movie.cinema.city.Movie
import movie.cinema.city.Ticket
import movie.cinema.city.model.BookingDetailResponse
import movie.cinema.city.model.BookingResponse
import java.util.Date

internal data class TicketFromResponse(
    private val booking: BookingResponse,
    private val detail: BookingDetailResponse,
    override val movie: Movie,
    override val cinema: Cinema
) : Ticket {

    override val id: String
        get() = booking.id
    override val name: String
        get() = booking.name
    override val startsAt: Date
        get() = booking.startsAt
    override val paidAt: Date?
        get() = booking.paidAt.takeIf { booking.isPaid }
    override val venue: Ticket.Venue = Venue()

    private inner class Venue : Ticket.Venue {
        override val name: String
            get() = detail.hall
        override val reservations: List<Ticket.Reservation>
            get() = detail.tickets.map(TicketFromResponse::Reservation)
    }

    private class Reservation(
        private val ticket: BookingDetailResponse.Ticket
    ) : Ticket.Reservation {
        override val row: String
            get() = ticket.row
        override val seat: String
            get() = ticket.seat
    }

}