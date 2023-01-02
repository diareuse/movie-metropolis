package movie.metropolis.app.screen.booking

import android.content.Context
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.TicketShareRegistry
import movie.core.model.Booking
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.io.File
import kotlin.test.assertIs

class BookingFacadeTest : FeatureTest() {

    private lateinit var facade: BookingFacade
    private lateinit var share: TicketShareRegistry
    private lateinit var context: Context

    override fun prepare() {
        context = mock {
            on { cacheDir }.thenReturn(File("build/cache"))
        }
        share = mock {
            on { runBlocking { get(any()) } }.thenReturn(ByteArray(0))
        }
        facade = FacadeModule().booking(user, user, share, context)
    }

    @Test
    fun returns_listFromResponse() = runTest {
        whenever(user.getBookings()).thenReturn(Result.success(listOf(mock<Booking.Active>())))
        val result = facade.getBookings()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().isNotEmpty())
    }

    @Test
    fun returns_singleActive() = runTest {
        whenever(user.getBookings()).thenReturn(
            Result.success(listOf(mock<Booking.Active>(), mock<Booking.Expired>()))
        )
        val result = facade.getBookings()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().count { it is BookingView.Active } == 1) { result }
    }

    @Test
    fun returns_multipleExpired() = runTest {
        whenever(user.getBookings()).thenReturn(
            Result.success(listOf(mock<Booking.Active>(), mock<Booking.Expired>()))
        )
        val result = facade.getBookings()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().count { it is BookingView.Expired } == 1) { result }
    }

    @Test
    fun returns_failureWhenSignedOff() = runTest {
        whenever(user.getBookings()).thenThrow(SecurityException())
        val result = facade.getBookings()
        assert(result.isFailure) { result }
        assertIs<SecurityException>(result.exceptionOrNull())
    }

    @Test
    fun returns_failure() = runTest {
        whenever(user.getBookings()).thenThrow(RuntimeException())
        val result = facade.getBookings()
        assert(result.isFailure) { result }
    }

}