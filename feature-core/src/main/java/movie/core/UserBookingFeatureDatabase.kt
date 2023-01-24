package movie.core

import movie.core.EventCinemaFeature.Companion.get
import movie.core.EventDetailFeature.Companion.get
import movie.core.adapter.BookingActiveFromDatabase
import movie.core.adapter.BookingExpiredFromDatabase
import movie.core.adapter.MovieFromId
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

    override suspend fun get(callback: ResultCallback<List<Booking>>) {
        val output = booking.selectAll().mapNotNull { booking ->
            val movie = movie.get(MovieFromId(booking.movieId)).getOrNull()
            val cinema = cinema.get(null).getOrNull()?.firstOrNull { it.id == booking.cinemaId }
            if (movie == null || cinema == null) return@mapNotNull null
            booking.asBooking(movie, cinema)
        }
        callback(Result.success(output))
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

    private suspend fun getSeats(booking: BookingStored): List<BookingSeatsView> {
        return seats.select(booking.id)
    }

    private operator fun Date.plus(duration: Duration): Date {
        return Date(time + duration.inWholeMilliseconds)
    }

}