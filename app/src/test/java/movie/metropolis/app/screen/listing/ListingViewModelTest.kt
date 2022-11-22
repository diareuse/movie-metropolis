package movie.metropolis.app.screen.listing

import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import movie.metropolis.app.screen.getOrThrow
import org.junit.Test
import kotlin.test.assertEquals

class ListingViewModelTest : ViewModelTest() {

    private lateinit var viewModel: ListingViewModel

    override fun prepare() {
        viewModel = ListingViewModel()
    }

    @Test
    fun current_returnsList() = runTest {
        responder.onUrlRespond(
            UrlResponder.MoviesByShowing("SHOWING"),
            "data-api-service-films-by-showing-type-SHOWING.json"
        )
        val loadable = viewModel.current
            .dropWhile { it.isLoading }
            .first()
        val result = loadable.getOrThrow()
        assertEquals(34, result.size, "Expected 34 elements, but was: $result")
    }

    @Test(expected = Throwable::class)
    fun current_failsGracefully() = runTest {
        viewModel.current
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test
    fun upcoming_returnsList() = runTest {
        responder.onUrlRespond(
            UrlResponder.MoviesByShowing("FUTURE"),
            "data-api-service-films-by-showing-type-FUTURE.json"
        )
        val loadable = viewModel.upcoming
            .dropWhile { it.isLoading }
            .first()
        val result = loadable.getOrThrow()
        assertEquals(18, result.size, "Expected 18 elements, but was: $result")
    }

    @Test(expected = Throwable::class)
    fun upcoming_failsGracefully() = runTest {
        viewModel.upcoming
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

}