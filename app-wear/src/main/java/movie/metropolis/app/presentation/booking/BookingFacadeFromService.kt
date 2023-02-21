package movie.metropolis.app.presentation.booking

import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.adapter.TicketViewFromDataMap
import movie.metropolis.app.presentation.OnChangedListener
import movie.wear.WearService

class BookingFacadeFromService(
    private val service: WearService
) : BookingFacade {

    override suspend fun get(): Result<List<TicketView>> {
        return service.get("/bookings")
            .getDataMapArrayList("bookings")
            ?.map(::TicketViewFromDataMap).orEmpty()
            .let(Result.Companion::success)
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