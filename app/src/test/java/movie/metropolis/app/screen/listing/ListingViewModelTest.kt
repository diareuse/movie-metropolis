package movie.metropolis.app.screen.listing

import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

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
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<List<MovieView>>>(loadable)
        val result = loadable.result
        assertEquals(34, result.size, "Expected 34 elements, but was: $result")
    }

    @Test
    fun current_failsGracefully() = runTest {
        val loadable = viewModel.current
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

    @Test
    fun upcoming_returnsList() = runTest {
        responder.onUrlRespond(
            UrlResponder.MoviesByShowing("FUTURE"),
            "data-api-service-films-by-showing-type-FUTURE.json"
        )
        val loadable = viewModel.upcoming
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<List<MovieView>>>(loadable)
        val result = loadable.result
        assertEquals(18, result.size, "Expected 18 elements, but was: $result")
    }

    @Test
    fun upcoming_failsGracefully() = runTest {
        val loadable = viewModel.upcoming
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

}