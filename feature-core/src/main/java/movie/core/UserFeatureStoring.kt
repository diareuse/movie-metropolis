package movie.core

import movie.core.adapter.asStored
import movie.core.db.dao.BookingDao
import movie.core.db.dao.BookingSeatsDao
import movie.core.model.Booking

class UserFeatureStoring(
    private val origin: UserFeature,
    private val bookingDao: BookingDao,
    private val bookingSeatsDao: BookingSeatsDao
) : UserFeature by origin {

    override suspend fun getBookings() = origin.getBookings().onSuccess {
        for (booking in it) {
            bookingDao.insertOrUpdate(booking.asStored())
            when (booking) {
                is Booking.Expired -> bookingSeatsDao.deleteFor(booking.id)
                is Booking.Active -> for (seat in booking.seats)
                    bookingSeatsDao.insertOrUpdate(seat.asStored(booking))
            }
        }
    }

}