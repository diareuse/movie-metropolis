package movie.metropolis.app.presentation.booking

import movie.metropolis.app.model.TicketView
import movie.metropolis.app.presentation.OnChangedListener

class BookingsFacadeRecover(
    private val origin: BookingsFacade
) : BookingsFacade {

    override suspend fun get(): Result<List<TicketView>> {
        return origin.runCatching { get().getOrThrow() }
    }

    override fun addOnChangedListener(listener: OnChangedListener): OnChangedListener {
        return origin.addOnChangedListener(listener)
    }

    override fun removeOnChangedListener(listener: OnChangedListener) {
        origin.removeOnChangedListener(listener)
    }

}