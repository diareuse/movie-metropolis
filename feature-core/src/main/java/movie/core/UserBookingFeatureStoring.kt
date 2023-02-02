package movie.core

import kotlinx.coroutines.coroutineScope
import movie.core.adapter.asStored
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.model.Booking

class UserBookingFeatureStoring(
    private val origin: UserBookingFeature,
    private val bookingDao: BookingDao,
    private val bookingSeatsDao: BookingSeatsDao
) : UserBookingFeature by origin {

    override suspend fun get(callback: ResultCallback<List<Booking>>) = coroutineScope {
        origin.get(callback.then(this) {
            for (booking in it) {
                bookingDao.insertOrUpdate(booking.asStored())
                when (booking) {
                    is Booking.Expired -> bookingSeatsDao.deleteFor(booking.id)
                    is Booking.Active -> for (seat in booking.seats)
                        bookingSeatsDao.insertOrUpdate(seat.asStored(booking))
                }
            }
        })
    }

}