package movie.core

import kotlinx.coroutines.runBlocking
import movie.core.di.TicketShareRegistryModule
import movie.core.model.Booking
import movie.core.model.Cinema
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import java.util.Date
import kotlin.test.assertEquals

class TicketShareRegistryTest {

    private lateinit var store: TicketStore
    private lateinit var registry: TicketShareRegistry

    @Before
    fun prepare() {
        val module = TicketShareRegistryModule()
        store = module.store()
        registry = TicketShareRegistryModule().registry(store = store)
    }

    @Test
    fun add_appendsToStore() = runBlocking {
        registry.add("af)AAAAAAA`~{8|,aObaUv7RmwR.:mja\"[v7ow\$!:4B.cjASQJs)oU@OPcLJ<,CFBdEv=njteEjT\"hfd?0k&kq;Bks/LRTm!3W!YfeLy}Pv(LY^9^QAAAA".encodeToByteArray())
        assertEquals(1, store.getAll().size)
    }

    @Test
    fun get_returnsFormattedString() = runBlocking {
        // jdks apparently don't have identical implementation for Gzip, so this miserably fails on any other than bundle with AS (:
        /*assertEquals(
            "af)AAAAAAA`~{8|,aO.DXXJasbzzpEx5/Rz|2ZzbPt\$+YX57`R>f.:\$0{GgsRzAcfeLy}P0kmeGvSAAAA",
            registry.get(TestBooking).decodeToString()
        )*/
    }

    companion object {

        private val TestCinema = mock<Cinema> {
            on { id }.thenReturn("id")
        }
        private val TestBooking = mock<Booking> {
            on { id }.thenReturn("id")
            on { startsAt }.thenReturn(Date(123))
            on { hall }.thenReturn("hall")
            on { movieId }.thenReturn("id")
            on { eventId }.thenReturn("eventId")
            on { cinema }.thenReturn(TestCinema)
            on { seats }.thenReturn(emptyList())
            on { expired }.thenReturn(false)
        }

    }

}