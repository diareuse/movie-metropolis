package movie.metropolis.app.presentation.booking

import movie.core.Recoverable
import movie.log.logSevere

class BookingFacadeRecover(
    private val origin: BookingFacade
) : BookingFacade by origin, Recoverable {

    override fun refresh() {
        origin.runCatching { refresh() }.logSevere()
    }

}