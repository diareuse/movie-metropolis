@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.TicketShareRegistry
import movie.core.model.Booking
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewActiveFromFeature
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.callback
import movie.metropolis.app.util.thenBlocking
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertFails
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BookingFacadeTest : FeatureTest() {

    private lateinit var facade: BookingFacade
    private lateinit var share: TicketShareRegistry

    override fun prepare() {
        share = mock {
            on { runBlocking { get(any()) } }.thenReturn(ByteArray(0))
        }
        facade = FacadeModule().booking(booking, share)
    }

    @Test
    fun getBookings_returns_values() = runTest {
        booking_responds_success()
        for (result in facade.getBookings())
            assert(result.getOrThrow().isNotEmpty())
    }

    @Test
    fun returns_failureWhenSignedOff() = runTest {
        booking_responds_failure(SecurityException())
        for (result in facade.getBookings())
            assertIs<SecurityException>(result.exceptionOrNull())
    }

    @Test
    fun returns_failure() = runTest {
        booking_responds_failure(RuntimeException())
        for (result in facade.getBookings())
            assertFails { result.getOrThrow() }
    }

    @Test
    fun getImage_returns_null() = runTest {
        assertNull(facade.getImage(mock()))
    }

    @Test
    fun getImage_returns_data() = runTest {
        val view = BookingViewActiveFromFeature(mock())
        val image = facade.getImage(view)
        assertNotNull(image)
    }

    @Test
    fun refresh_pingsBookings() = runTest {
        facade.refresh()
        verify(booking).invalidate()
    }

    // ---

    private suspend fun BookingFacade.getBookings(): List<Result<List<BookingView>>> {
        val outputs = mutableListOf<Result<List<BookingView>>>()
        getBookings { outputs += it }
        return outputs
    }

    private fun booking_responds_failure(throwable: Throwable = SecurityException()) {
        wheneverBlocking { booking.get(any()) }.thenThrow(throwable)
    }

    private fun booking_responds_success() {
        wheneverBlocking { booking.get(any()) }.thenBlocking {
            callback(0) {
                Result.success(listOf(mock<Booking.Active>(), mock<Booking.Expired>()))
            }
        }
    }

}