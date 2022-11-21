package movie.metropolis.app.screen.detail

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertIs

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
        val loadable = viewModel.detail
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<MovieDetailView>>(loadable)
    }

    @Test
    fun detail_failsGracefully() = runTest {
        val loadable = viewModel.detail
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

    @Test
    fun trailer_returnsDetail() = runTest {
        responder.onUrlRespond(
            UrlResponder.Detail(movie),
            "data-api-service-films-byDistributorCode.json"
        )
        val loadable = viewModel.trailer
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<VideoView>>(loadable)
    }

    @Test
    fun trailer_failsGracefully() = runTest {
        val loadable = viewModel.trailer
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

    @Test
    fun poster_returnsDetail() = runTest {
        responder.onUrlRespond(
            UrlResponder.Detail(movie),
            "data-api-service-films-byDistributorCode.json"
        )
        val loadable = viewModel.poster
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<ImageView>>(loadable)
    }

    @Test
    fun poster_failsGracefully() = runTest {
        val loadable = viewModel.poster
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

}