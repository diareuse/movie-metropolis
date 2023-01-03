package movie.metropolis.app.screen.booking

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.TicketShareRegistry
import movie.core.UserFeature
import movie.core.model.Booking
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.model.adapter.BookingViewActiveFromFeature
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.spy
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertIs
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class BookingFacadeTest : FeatureTest() {

    private lateinit var online: UserFeature
    private lateinit var facade: BookingFacade
    private lateinit var share: TicketShareRegistry

    override fun prepare() {
        share = mock {
            on { runBlocking { get(any()) } }.thenReturn(ByteArray(0))
        }
        online = spy(user)
        facade = FacadeModule().booking(user, online, share)
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
        verify(online).getBookings()
    }

}