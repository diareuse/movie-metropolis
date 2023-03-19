package movie.core.adapter

import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.nwk.model.BookingResponse
import java.util.Date

internal data class BookingExpiredFromResponse(
    private val response: BookingResponse,
    override val cinema: Cinema
) : Booking.Expired {

    constructor(
        response: BookingResponse,
        cinemas: Iterable<Cinema>
    ) : this(
        response = response,
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
    override val movieId: String
        get() = response.movieId

}