package movie.core

import movie.core.EventCinemaFeature.Companion.get
import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.BookingActiveFromResponse
import movie.core.adapter.BookingExpiredFromResponse
import movie.core.adapter.MovieDetailFromId
import movie.core.adapter.MovieFromId
import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.MovieDetail
import movie.core.nwk.UserService
import movie.core.nwk.model.BookingResponse

class UserBookingFeatureNetwork(
    private val service: UserService,
    private val cinema: EventCinemaFeature,
    private val detail: EventDetailFeature
) : UserBookingFeature {

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        val bookings = service.getBookings().getOrThrow()
        val cinemas = cinema.get(null).getOrThrow().toList()
        var outputs = bookings.map { it.asBooking(MovieDetailFromId(it.movieId), cinemas) }
        callback(Result.success(outputs))

        outputs = bookings.mapNotNull {
            val movie = detail.get(MovieFromId(it.movieId)).getOrNull() ?: return@mapNotNull null
            it.asBooking(movie, cinemas)
        }
        callback(Result.success(outputs))
    }

    override fun invalidate() = Unit

    // ---

    private suspend fun BookingResponse.asBooking(
        movie: MovieDetail,
        cinemas: List<Cinema>
    ) = when (isExpired(movie.duration)) {
        true -> BookingExpiredFromResponse(this, movie, cinemas)
        else -> BookingActiveFromResponse(this, getDetail(this), movie, cinemas)
    }

    private suspend fun getDetail(booking: BookingResponse) =
        service.getBooking(booking.id).getOrThrow()

}