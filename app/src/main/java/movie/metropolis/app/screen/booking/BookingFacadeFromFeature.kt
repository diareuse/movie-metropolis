package movie.metropolis.app.screen.booking

import movie.metropolis.app.feature.global.Booking
import movie.metropolis.app.feature.global.UserFeature
import movie.metropolis.app.model.BookingViewActiveFromFeature
import movie.metropolis.app.model.BookingViewExpiredFromFeature

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