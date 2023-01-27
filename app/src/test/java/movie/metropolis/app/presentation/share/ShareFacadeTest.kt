package movie.metropolis.app.presentation.share

import kotlinx.coroutines.test.runTest
import movie.core.TicketShareRegistry
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.nio.IntBuffer

class ShareFacadeTest : FeatureTest() {

    private lateinit var facade: ShareFacade
    private lateinit var share: TicketShareRegistry

    override fun prepare() {
        share = mock()
        facade = FacadeModule().share(share, booking)
    }

    @Test
    fun putTicket_accepts_text() = runTest {
        facade.putTicket(TicketRepresentation.Text("foo"))
        verify(share).add("foo".encodeToByteArray())
    }

    @Test
    fun putTicket_accepts_image() = runTest {
        val file = Thread.currentThread().contextClassLoader?.getResourceAsStream("image.data")
            ?.bufferedReader()
            ?.readLines().orEmpty()
            .map { row -> row.split(",").map { it.toInt() } }
        val pixels = file.flatten()
        val buffer = IntBuffer.allocate(pixels.size)
        buffer.put(pixels.toIntArray())
        facade.putTicket(TicketRepresentation.Image(file[0].size, file.size, buffer))
        verify(share).add("foo".encodeToByteArray())
    }

    @Test
    fun putTicket_updates_tickets() = runTest {
        facade.putTicket(TicketRepresentation.Text("foo"))
        verify(booking).get(any())
    }

}