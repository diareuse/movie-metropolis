package movie.metropolis.app.presentation.booking

import androidx.core.net.toUri
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataClient.OnDataChangedListener
import com.google.android.gms.wearable.DataEventBuffer
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.adapter.TicketViewFromDataMap
import movie.metropolis.app.presentation.OnChangedListener
import movie.wear.WearService

class BookingFacadeFromService(
    private val service: WearService,
    private val client: DataClient
) : BookingFacade {

    override suspend fun get(): Result<List<TicketView>> {
        return service.get("/bookings")
            .getDataMapArrayList("bookings")
            ?.map(::TicketViewFromDataMap).orEmpty()
            .let(Result.Companion::success)
    }

    override fun addOnChangedListener(listener: OnChangedListener): OnChangedListener {
        val listener = CombinedListener(listener)
        client.addListener(listener, "wear://*/bookings".toUri(), DataClient.FILTER_LITERAL)
            .addOnCompleteListener {}
        return listener
    }

    override fun removeOnChangedListener(listener: OnChangedListener) {
        require(listener is CombinedListener) { "Use listener provided as return value of \"addOnChangedListener\"" }
        client.removeListener(listener)
            .addOnCompleteListener {}
    }

    private class CombinedListener(
        listener: OnChangedListener
    ) : OnChangedListener by listener, OnDataChangedListener {
        override fun onDataChanged(p0: DataEventBuffer) = onChanged()
    }

}