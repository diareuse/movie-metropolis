package movie.core.adapter

import movie.core.db.model.BookingSeatsView
import movie.core.db.model.BookingStored
import movie.core.model.Booking
import movie.core.model.Cinema
import java.util.Date

data class BookingFromDatabase(
    private val bookingStored: BookingStored,
    override val cinema: Cinema,
    private val seatsStored: List<BookingSeatsView>,
    override val expired: Boolean
) : Booking {
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
    override val hall: String
        get() = bookingStored.hall.orEmpty()
    override val seats: List<Booking.Seat>
        get() = seatsStored.map(BookingFromDatabase::SeatFromDatabase)

    private data class SeatFromDatabase(
        private val stored: BookingSeatsView
    ) : Booking.Seat {
        override val row: String
            get() = stored.row
        override val seat: String
            get() = stored.seat
    }
}