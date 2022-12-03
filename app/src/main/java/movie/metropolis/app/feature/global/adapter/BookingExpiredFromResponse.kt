package movie.metropolis.app.feature.global.adapter

import movie.metropolis.app.feature.global.Booking
import movie.metropolis.app.feature.global.Cinema
import movie.metropolis.app.feature.global.MovieDetail
import movie.metropolis.app.feature.global.model.BookingResponse
import java.util.Date

internal data class BookingExpiredFromResponse(
    private val response: BookingResponse,
    override val movie: MovieDetail,
    override val cinema: Cinema
) : Booking.Expired {

    constructor(
        response: BookingResponse,
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