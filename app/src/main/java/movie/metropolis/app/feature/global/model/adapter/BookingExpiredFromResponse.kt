package movie.metropolis.app.feature.global.model.adapter

import movie.metropolis.app.feature.global.model.Booking
import movie.metropolis.app.feature.global.model.Cinema
import movie.metropolis.app.feature.global.model.MovieDetail
import movie.metropolis.app.feature.global.model.remote.BookingResponse
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