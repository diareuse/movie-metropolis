package movie.core.adapter

import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.MovieDetail
import java.util.Date

internal data class BookingExpiredFromResponse(
    private val response: movie.core.nwk.model.BookingResponse,
    override val movie: MovieDetail,
    override val cinema: Cinema
) : Booking.Expired {

    constructor(
        response: movie.core.nwk.model.BookingResponse,
        movie: MovieDetail,
        cinemas: List<Cinema>
    ) : this(
        response = response,
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

}