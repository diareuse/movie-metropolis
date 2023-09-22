package movie.metropolis.app.presentation.booking

import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.adapter.TicketViewActiveFromDataMap
import movie.metropolis.app.model.adapter.TicketViewExpiredFromDataMap
import movie.metropolis.app.presentation.OnChangedListener
import movie.wear.WearService

class BookingsFacadeFromService(
    private val service: WearService
) : BookingsFacade {

    override suspend fun get(): Result<List<TicketView>> {
        val active = service.get("/bookings/active")
            .getDataMapArrayList("bookings")
            ?.map(::TicketViewActiveFromDataMap).orEmpty()
        val expired = service.get("/bookings/expired")
            .getDataMapArrayList("bookings")
            ?.map(::TicketViewExpiredFromDataMap).orEmpty()
        return Result.success(active + expired)
    }

    override fun addOnChangedListener(listener: OnChangedListener): OnChangedListener {
        val listener = service.addListener("/bookings") { listener.onChanged() }
        return CombinedListener(listener)
    }

    override fun removeOnChangedListener(listener: OnChangedListener) {
        require(listener is CombinedListener) { "Use listener provided as return value of \"addOnChangedListener\"" }
        service.removeListener(listener.listener)
    }

    private class CombinedListener(
        val listener: WearService.OnChangedListener
    ) : OnChangedListener, WearService.OnChangedListener by listener

}