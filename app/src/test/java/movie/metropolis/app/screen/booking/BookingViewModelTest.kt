package movie.metropolis.app.screen.booking

import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import movie.metropolis.app.screen.getOrThrow
import org.junit.Test
import kotlin.test.assertEquals

class BookingViewModelTest : ViewModelTest() {

    private lateinit var viewModel: BookingViewModel

    override fun prepare() {
        viewModel = BookingViewModel()
    }

    @Test
    fun expired_returnsList() = runTest {
        responder.onUrlRespond(UrlResponder.Booking, "group-customer-service-bookings.json")
        val loadable = viewModel.expired
            .dropWhile { it.isLoading }
            .first()
        val result = loadable.getOrThrow()
        assertEquals(10, result.size, "Expected to contain 10 elements, was: $result")
    }

    @Test
    fun active_returnsList() = runTest {
        responder.onUrlRespond(UrlResponder.Booking, "group-customer-service-bookings.json")
        val loadable = viewModel.active
            .dropWhile { it.isLoading }
            .first()
        val result = loadable.getOrThrow()
        assertEquals(1, result.size, "Expected to contain 1 element, was: $result")
    }

    @Test(expected = Throwable::class)
    fun expired_failsGracefully() = runTest {
        viewModel.active
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test(expected = Throwable::class)
    fun active_failsGracefully() = runTest {
        viewModel.active
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

}