package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import movie.metropolis.app.screen.getOrThrow
import org.junit.Test

class MovieViewModelTest : ViewModelTest() {

    private lateinit var movie: String
    private lateinit var viewModel: MovieViewModel

    override fun prepare() {
        movie = "123abc"
        viewModel = MovieViewModel(SavedStateHandle(mapOf("movie" to movie)))
    }

    @Test
    fun detail_returnsDetail() = runTest {
        responder.onUrlRespond(
            UrlResponder.Detail(movie),
            "data-api-service-films-byDistributorCode.json"
        )
        viewModel.detail
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test(expected = Throwable::class)
    fun detail_failsGracefully() = runTest {
        viewModel.detail
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test
    fun trailer_returnsDetail() = runTest {
        responder.onUrlRespond(
            UrlResponder.Detail(movie),
            "data-api-service-films-byDistributorCode.json"
        )
        viewModel.trailer
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test(expected = Throwable::class)
    fun trailer_failsGracefully() = runTest {
        viewModel.trailer
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test
    fun poster_returnsDetail() = runTest {
        responder.onUrlRespond(
            UrlResponder.Detail(movie),
            "data-api-service-films-byDistributorCode.json"
        )
        viewModel.poster
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

    @Test(expected = Throwable::class)
    fun poster_failsGracefully() = runTest {
        viewModel.poster
            .dropWhile { it.isLoading }
            .first()
            .getOrThrow()
    }

}