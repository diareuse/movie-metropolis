package movie.metropolis.app.feature.global.model.adapter

import movie.metropolis.app.feature.global.model.Booking
import movie.metropolis.app.feature.global.model.Cinema
import movie.metropolis.app.feature.global.model.MovieDetail
import movie.metropolis.app.feature.global.model.remote.BookingDetailResponse
import movie.metropolis.app.feature.global.model.remote.BookingResponse
import java.util.Date

internal data class BookingActiveFromResponse(
    private val response: BookingResponse,
    private val detail: BookingDetailResponse,
    override val movie: MovieDetail,
    override val cinema: Cinema
) : Booking.Active {

    constructor(
        response: BookingResponse,
        detail: BookingDetailResponse,
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
        private val ticket: BookingDetailResponse.Ticket
    ) : Booking.Active.Seat {

        override val row: String
            get() = ticket.row
        override val seat: String
            get() = ticket.seat

    }
}