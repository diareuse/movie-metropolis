package movie.metropolis.app.screen.booking

import movie.log.flatMapCatching
import movie.log.logSevere
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade {

    override suspend fun getBookings() =
        origin.flatMapCatching { getBookings() }.logSevere()

    override suspend fun refresh() {
        origin.runCatching { refresh() }.logSevere()
    }

    override suspend fun getImage(view: BookingView): Image? {
        return origin.runCatching { getImage(view) }.logSevere().getOrNull()
    }

}