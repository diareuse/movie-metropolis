package movie.metropolis.app.screen.booking

import movie.core.Recoverable
import movie.core.ResultCallback
import movie.core.result
import movie.log.logSevere
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.facade.Image

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade, Recoverable {

    override suspend fun getBookings(callback: ResultCallback<List<BookingView>>) {
        runCatchingResult(callback.result { it.logSevere() }) {
            origin.getBookings(it)
        }
    }

    override suspend fun refresh() {
        origin.runCatching { refresh() }.logSevere()
    }

    override suspend fun getImage(view: BookingView): Image? {
        return origin.runCatching { getImage(view) }.logSevere().getOrNull()
    }

}