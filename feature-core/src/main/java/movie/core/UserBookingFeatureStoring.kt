package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import movie.core.adapter.asStored
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.model.Booking

class UserBookingFeatureStoring(
    private val origin: UserBookingFeature,
    private val bookingDao: BookingDao,
    private val bookingSeatsDao: BookingSeatsDao,
    private val scope: CoroutineScope
) : UserBookingFeature by origin {

    override suspend fun get() = origin.get().onSuccess {
        scope.launch {
            store(it)
        }
    }

    private suspend fun store(bookings: Sequence<Booking>) {
        for (booking in bookings) {
            bookingDao.insertOrUpdate(booking.asStored())
            for (seat in booking.seats)
                bookingSeatsDao.insertOrUpdate(seat.asStored(booking))
        }
    }

}