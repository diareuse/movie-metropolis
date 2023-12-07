package movie.core

import movie.core.adapter.asStored
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.model.Booking

class UserBookingFeatureStoring(
    private val origin: UserBookingFeature,
    private val bookingDao: BookingDao,
    private val bookingSeatsDao: BookingSeatsDao
) : UserBookingFeature by origin {

    override suspend fun get(): Sequence<Booking> = origin.get().also {
        store(it)
    }

    private suspend fun store(bookings: Sequence<Booking>) {
        for (booking in bookings) {
            bookingDao.insertOrUpdate(booking.asStored())
            for (seat in booking.seats)
                bookingSeatsDao.insertOrUpdate(seat.asStored(booking))
        }
    }

}