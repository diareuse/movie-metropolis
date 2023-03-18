package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.adapter.BookingActiveFromDatabase
import movie.core.adapter.BookingExpiredFromDatabase
import movie.core.adapter.MovieDetailFromId
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.model.BookingSeatsView
import movie.core.db.model.BookingStored
import movie.core.model.Booking
import movie.core.model.Cinema
import movie.core.model.Movie
import movie.core.model.MovieDetail
import java.util.Date
import kotlin.time.Duration

class UserBookingFeatureDatabase(
    private val booking: BookingDao,
    private val seats: BookingSeatsDao,
    private val movie: EventDetailFeature,
    private val cinema: EventCinemaFeature,
) : UserBookingFeature {

    override suspend fun get(callback: ResultCallback<List<Booking>>) = coroutineScope {
        val cinemas = cinema.get(null).getOrNull()
        val bookings = booking.selectAll().mapNotNull { booking ->
            val movie = MovieDetailFromId(booking.movieId)
            val cinema = cinemas?.firstOrNull { it.id == booking.cinemaId }
                ?: return@mapNotNull null
            booking.asBooking(movie, cinema)
        }
        callback(Result.success(bookings))
        callback.parallelizeContinuous(this, bookings) { booking, callback ->
            movie.get(booking.movie).onSuccess { movie ->
                callback(booking.withDetail(movie))
            }
        }
    }

    override fun invalidate() = Unit

    // ---

    private fun BookingStored.isExpired(movie: Movie): Boolean {
        val expiresAt = startsAt + movie.duration
        return Date().after(expiresAt)
    }

    private suspend fun BookingStored.asBooking(
        movie: MovieDetail,
        cinema: Cinema
    ) = when (isExpired(movie)) {
        true -> BookingExpiredFromDatabase(this, movie, cinema)
        else -> BookingActiveFromDatabase(this, movie, cinema, getSeats(this))
    }

    private fun Booking.withDetail(
        movie: MovieDetail
    ) = when (this) {
        is Booking.Active -> BookingActiveWithDetail(this, movie)
        is Booking.Expired -> BookingExpiredWithDetail(this, movie)
    }

    private suspend fun getSeats(booking: BookingStored): List<BookingSeatsView> {
        return seats.select(booking.id)
    }

    private operator fun Date.plus(duration: Duration): Date {
        return Date(time + duration.inWholeMilliseconds)
    }

}

data class BookingActiveWithDetail(
    private val origin: Booking.Active,
    override val movie: MovieDetail
) : Booking.Active by origin

data class BookingExpiredWithDetail(
    private val origin: Booking.Expired,
    override val movie: MovieDetail
) : Booking.Expired by origin