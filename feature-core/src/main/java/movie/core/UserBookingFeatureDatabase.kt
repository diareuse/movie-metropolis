package movie.core

import movie.core.adapter.BookingFromDatabase
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.db.model.BookingSeatsView
import movie.core.db.model.BookingStored
import movie.core.model.Booking
import movie.core.model.Cinema
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

class UserBookingFeatureDatabase(
    private val booking: BookingDao,
    private val seats: BookingSeatsDao,
    private val cinema: EventCinemaFeature,
) : UserBookingFeature {

    override suspend fun get(): Sequence<Booking> {
        val cinemas = cinema.runCatching { get(null) }.getOrNull()?.associateBy { it.id }.orEmpty()
        return booking.selectAll()
            .mapNotNull { it.asBooking(cinemas[it.cinemaId] ?: return@mapNotNull null) }
            .asSequence()
    }

    override fun invalidate() = Unit

    // ---

    private fun BookingStored.isExpired(): Boolean {
        val expiresAt = startsAt + 3.hours
        return Date().after(expiresAt)
    }

    private suspend fun BookingStored.asBooking(
        cinema: Cinema
    ) = BookingFromDatabase(
        bookingStored = this,
        cinema = cinema,
        seatsStored = getSeats(this),
        expired = isExpired()
    )

    private suspend fun getSeats(booking: BookingStored): List<BookingSeatsView> {
        return seats.select(booking.id)
    }

    private operator fun Date.plus(duration: Duration): Date {
        return Date(time + duration.inWholeMilliseconds)
    }

}