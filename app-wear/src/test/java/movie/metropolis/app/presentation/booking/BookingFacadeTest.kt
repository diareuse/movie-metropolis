package movie.metropolis.app.presentation.booking

import com.google.android.gms.wearable.DataMap
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.adapter.TicketViewActiveFromDataMap
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.util.wheneverBlocking
import movie.wear.WearService
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import java.util.Date
import kotlin.test.assertEquals

class BookingFacadeTest {

    private lateinit var facade: BookingsFacade
    private lateinit var service: WearService

    @Before
    fun prepare() {
        service = mock()
        facade = FacadeModule().booking(service)
    }

    @Test
    fun get_returns_tickets() = runTest {
        val expected = service_returns_success()
        val actual = facade.get().getOrThrow()
        assertEquals(expected, actual)
    }

    @Test
    fun listener_notifies() = runTest {
        val currentListener = service_handles_listener()
        val listener = mock<OnChangedListener>()
        facade.addOnChangedListener(listener)
        currentListener()?.onChanged()
        verify(listener).onChanged()
    }

    @Test
    fun listener_removes() = runTest {
        val currentListener = service_handles_listener()
        val listener = mock<OnChangedListener>()
        val removable = facade.addOnChangedListener(listener)
        facade.removeOnChangedListener(removable)
        currentListener()?.onChanged()
        verifyNoInteractions(listener)
    }

    // ---

    @Suppress("RedundantUnitExpression")
    private fun service_handles_listener(path: String = "/bookings"): () -> WearService.OnChangedListener? {
        var listener = null as WearService.OnChangedListener?
        wheneverBlocking { service.addListener(eq(path), any()) }.then {
            listener = it.getArgument<WearService.OnChangedListener>(1)
            listener
        }
        wheneverBlocking { service.removeListener(any()) }.then {
            listener = null
            Unit
        }
        return { listener }
    }

    private fun service_returns_success(
        path: String = "/bookings",
        items: List<DataMap> = listOf(TicketView())
    ): List<TicketView> {
        val data = DataMap().apply {
            putDataMapArrayList("bookings", ArrayList(items))
        }
        wheneverBlocking { service.get(path) }.thenReturn(data)
        return items.map(::TicketViewActiveFromDataMap)
    }

    private fun TicketView(
        id: String = "id",
        cinema: String = "cinema",
        startsAt: Date = Date(1),
        hall: String = "hall",
        seats: List<DataMap> = listOf(TicketViewSeat()),
        name: String = "name"
    ) = DataMap().apply {
        putString("id", id)
        putString("cinema", cinema)
        putLong("starts_at", startsAt.time)
        putString("hall", hall)
        putDataMapArrayList("seats", ArrayList(seats))
        putString("name", name)
    }

    private fun TicketViewSeat(
        row: String = "10",
        seat: String = "15"
    ) = DataMap().apply {
        putString("row", row)
        putString("seat", seat)
    }

}