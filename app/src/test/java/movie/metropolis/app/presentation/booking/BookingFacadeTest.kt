@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.TicketShareRegistry
import movie.core.model.Booking
import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.image.Swatch
import movie.image.SwatchColor
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewFromFeature
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BookingFacadeTest : FeatureTest() {

    private lateinit var facade: BookingFacade
    private lateinit var share: TicketShareRegistry

    override fun prepare() {
        share = mock {
            on { runBlocking { get(any()) } }.thenReturn("foo".toByteArray())
        }
        facade = FacadeModule().booking(booking, share, detail)
    }

    @Test
    fun bookings_returns_values() = runTest {
        booking_responds_success()
        detail_responds_success()
        for (result in facade.getBookings())
            assert(result.isNotEmpty())
    }

    @Test
    fun bookings_returns_detail() = runTest {
        booking_responds_success()
        val expected = detail_responds_success()
        val result = facade.getBookings().last()
        for (item in result)
        // directors are available only from detail
            assertEquals(expected.directors, item.movie.directors.map { it.name })
    }

    @Test
    fun returns_failureWhenSignedOff() = runTest {
        booking_responds_failure(SecurityException())
        assertFailsWith(SecurityException::class) {
            facade.getBookings()
        }
    }

    @Test
    fun returns_failure() = runTest {
        booking_responds_failure(RuntimeException())
        assertFails {
            facade.getBookings()
        }
    }

    @Test(expected = IllegalStateException::class)
    fun getImage_returns_null() = runTest {
        assertNull(facade.getShareImage(mock()))
    }

    @Test
    fun getImage_returns_data() = runTest {
        val view = BookingViewFromFeature(mock())
        val image = facade.getShareImage(view)
        assertNotNull(image)
    }

    @Test
    fun refresh_pingsBookings() = runTest {
        facade.refresh()
        verify(booking).invalidate()
    }

    // ---

    private suspend fun BookingFacade.getBookings(): List<List<BookingView>> {
        val outputs = mutableListOf<List<BookingView>>()
        bookings.collect { outputs += it }
        return outputs
    }

    private fun booking_responds_failure(throwable: Throwable = SecurityException()) {
        wheneverBlocking { booking.get() }.thenThrow(throwable)
    }

    private fun booking_responds_success() {
        val active = mock<Booking> {
            on { id }.thenReturn("")
            on { movieId }.thenReturn("")
            on { expired }.thenReturn(false)
        }
        val expired = mock<Booking> {
            on { id }.thenReturn("")
            on { movieId }.thenReturn("")
            on { expired }.thenReturn(true)
        }
        wheneverBlocking { booking.get() }.thenReturn(sequenceOf(active, expired))
    }

    private fun detail_responds_success(): MovieDetail {
        val movie = mock<MovieDetail> {
            on { directors }.thenReturn(listOf("a", "b", "c"))
            on { media }.thenReturn(listOf(Media.Image(0, 0, "")))
        }
        wheneverBlocking { detail.get(any()) }.thenReturn(movie)
        return movie
    }

    private fun analyzer_responds_success(): Int {
        val color = nextInt(0, 0xffffff)
        val swatch = Swatch(
            SwatchColor(color),
            SwatchColor(color),
            SwatchColor(color)
        )
        wheneverBlocking { analyzer.getColors(any()) }.thenReturn(swatch)
        return color
    }

}