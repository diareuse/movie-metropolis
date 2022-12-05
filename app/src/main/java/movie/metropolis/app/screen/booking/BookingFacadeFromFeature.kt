package movie.metropolis.app.screen.booking

import movie.core.UserFeature
import movie.core.model.Booking
import movie.metropolis.app.model.adapter.BookingViewActiveFromFeature
import movie.metropolis.app.model.adapter.BookingViewExpiredFromFeature

class BookingFacadeFromFeature(
    private val feature: UserFeature
) : BookingFacade {

    override suspend fun getBookings() = feature.getBookings()
        .map { it.map(::BookingViewFromFeature) }

    @Suppress("FunctionName")
    private fun BookingViewFromFeature(booking: Booking) = when (booking) {
        is Booking.Active -> BookingViewActiveFromFeature(booking)
        is Booking.Expired -> BookingViewExpiredFromFeature(booking)
    }

}