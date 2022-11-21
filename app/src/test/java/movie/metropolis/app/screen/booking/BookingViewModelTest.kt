package movie.metropolis.app.screen.booking

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class BookingViewModelTest : ViewModelTest() {

    private lateinit var viewModel: BookingViewModel

    override fun prepare() {
        viewModel = BookingViewModel()
    }

    @Test
    fun expired_returnsList() = runTest {
        responder.onUrlRespond(UrlResponder.Booking, "group-customer-service-bookings.json")
        val loadable = viewModel.expired
            .takeWhile { it !is Loadable.Loading<*> }
            .first()
        assertIs<Loadable.Loaded<List<BookingView>>>(loadable)
        val result = loadable.result
        assertEquals(10, result.size, "Expected to contain 10 elements, was: $result")
    }

    @Test
    fun active_returnsList() = runTest {
        responder.onUrlRespond(UrlResponder.Booking, "group-customer-service-bookings.json")
        val loadable = viewModel.active
            .takeWhile { it !is Loadable.Loading<*> }
            .filterIsInstance<Loadable.Loaded<List<BookingView>>>()
            .first().result
        assertIs<Loadable.Loaded<List<BookingView>>>(loadable)
        val result = loadable.result
        assertEquals(1, result.size, "Expected to contain 1 element, was: $result")
    }

    @Test
    fun expired_failsGracefully() = runTest {
        val result = viewModel.active
            .takeWhile { it !is Loadable.Loading<*> }
            .first()
        assertIs<Loadable.Error<List<BookingView>>>(result)
    }

    @Test
    fun active_failsGracefully() = runTest {
        val result = viewModel.active
            .takeWhile { it !is Loadable.Loading<*> }
            .first()
        assertIs<Loadable.Error<List<BookingView>>>(result)
    }

}