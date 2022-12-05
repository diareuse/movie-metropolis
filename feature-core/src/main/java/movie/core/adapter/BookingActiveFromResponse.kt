package movie.core.adapter

import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.MovieDetail
import java.util.Date

internal data class BookingActiveFromResponse(
    private val response: movie.core.nwk.model.BookingResponse,
    private val detail: movie.core.nwk.model.BookingDetailResponse,
    override val movie: MovieDetail,
    override val cinema: Cinema
) : Booking.Active {

    constructor(
        response: movie.core.nwk.model.BookingResponse,
        detail: movie.core.nwk.model.BookingDetailResponse,
        movie: MovieDetail,
        cinemas: List<Cinema>
    ) : this(
        response = response,
        detail = detail,
        movie = movie,
        cinema = cinemas.first { it.id == response.cinemaId }
    )

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val startsAt: Date
        get() = response.startsAt
    override val paidAt: Date
        get() = response.paidAt
    override val eventId: String
        get() = response.eventId
    override val hall: String
        get() = detail.hall
    override val seats: List<Booking.Active.Seat>
        get() = detail.tickets.map(BookingActiveFromResponse::SeatFromResponse)

    private data class SeatFromResponse(
        private val ticket: movie.core.nwk.model.BookingDetailResponse.Ticket
    ) : Booking.Active.Seat {

        override val row: String
            get() = ticket.row
        override val seat: String
            get() = ticket.seat

    }
}