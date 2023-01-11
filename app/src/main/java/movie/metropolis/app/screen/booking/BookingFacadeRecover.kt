package movie.metropolis.app.screen.booking

import movie.log.logCatching
import movie.log.logCatchingResult
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade {

    override suspend fun getBookings() =
        origin.logCatchingResult("booking") { getBookings() }

    override suspend fun refresh() {
        origin.logCatching("booking-refresh") { refresh() }
    }

    override suspend fun getImage(view: BookingView): Image? {
        return origin.logCatching("booking-image") { getImage(view) }.getOrNull()
    }

}