package movie.metropolis.app.presentation.booking

import movie.core.Recoverable
import movie.log.logSevere
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade by origin, Recoverable {

    override fun refresh() {
        origin.runCatching { refresh() }.logSevere()
    }

    override suspend fun getShareImage(view: BookingView): Image? {
        return origin.runCatching { getShareImage(view) }.logSevere().getOrNull()
    }

}