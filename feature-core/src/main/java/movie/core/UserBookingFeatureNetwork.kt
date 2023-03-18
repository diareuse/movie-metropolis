package movie.core

import kotlinx.coroutines.coroutineScope
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

    override suspend fun get(callback: ResultCallback<List<Booking>>) = coroutineScope {
        val bookings = service.getBookings().getOrThrow()
        val cinemas = cinema.get(null).getOrThrow().toList()
        val outputs = bookings.map { it.asBooking(MovieDetailFromId(it.movieId), cinemas) }
        callback(Result.success(outputs))
        callback.parallelize(this, outputs) {
            val movie = detail.get(MovieFromId(it.movie.id)).getOrNull()
            if (movie == null) it
            else it.update(movie)
        }
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

    private fun Booking.update(movie: MovieDetail) = when (this) {
        is BookingActiveFromResponse -> copy(movie = movie)
        is BookingExpiredFromResponse -> copy(movie = movie)
        else -> this
    }

    private suspend fun getDetail(booking: BookingResponse) =
        service.getBooking(booking.id).getOrThrow()

}