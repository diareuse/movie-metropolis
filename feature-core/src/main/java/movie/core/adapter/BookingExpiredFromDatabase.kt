package movie.core.adapter

import movie.core.db.model.BookingStored
import movie.core.model.Booking
import movie.core.model.Cinema
import java.util.Date

data class BookingExpiredFromDatabase(
    private val bookingStored: BookingStored,
    override val cinema: Cinema
) : Booking.Expired {
    override val id: String
        get() = bookingStored.id
    override val name: String
        get() = bookingStored.name
    override val startsAt: Date
        get() = bookingStored.startsAt
    override val paidAt: Date
        get() = bookingStored.paidAt
    override val eventId: String
        get() = bookingStored.eventId
    override val movieId: String
        get() = bookingStored.movieId
}