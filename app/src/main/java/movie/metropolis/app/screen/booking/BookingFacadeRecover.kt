package movie.metropolis.app.screen.booking

import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade {

    override suspend fun getBookings() =
        kotlin.runCatching { origin.getBookings().getOrThrow() }

    override suspend fun refresh() {
        kotlin.runCatching { origin.refresh() }
    }

    override suspend fun getImage(view: BookingView): Image? {
        return origin.runCatching { getImage(view) }.getOrNull()
    }

}