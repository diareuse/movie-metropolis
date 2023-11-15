package movie.core.adapter

import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.nwk.model.BookingDetailResponse
import movie.core.nwk.model.BookingResponse
import java.util.Date

internal data class BookingFromResponse(
    private val response: BookingResponse,
    private val detail: BookingDetailResponse,
    override val cinema: Cinema,

    ) : Booking {

    constructor(
        response: BookingResponse,
        detail: BookingDetailResponse,
        cinemas: Iterable<Cinema>
    ) : this(
        response = response,
        detail = detail,
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
    override val expired: Boolean
        get() = response.isExpired()
    override val movieId: String
        get() = response.movieId
    override val hall: String
        get() = detail.hall
    override val seats: List<Booking.Seat>
        get() = detail.tickets.map(BookingFromResponse::SeatFromResponse)

    private data class SeatFromResponse(
        private val ticket: BookingDetailResponse.Ticket
    ) : Booking.Seat {

        override val row: String
            get() = ticket.row
        override val seat: String
            get() = ticket.seat

    }
}