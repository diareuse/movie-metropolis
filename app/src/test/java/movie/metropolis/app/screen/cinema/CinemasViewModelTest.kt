package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.LocationSnapshot
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class CinemasViewModelTest : ViewModelTest() {

    private lateinit var viewModel: CinemasViewModel

    override fun prepare() {
        viewModel = CinemasViewModel()
    }

    @Test
    fun items_returnsList_withoutLocation() = runTest {
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        val loadable = viewModel.items
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<List<CinemaView>>>(loadable)
        val result = loadable.result
        assertEquals(14, result.size, "Expected to contain 14 elements, but was: $result")
    }

    @Test
    fun items_returnsList_withLocation() = runTest {
        val location = LocationSnapshot(50.0981848, 14.4506479)
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CinemaLocation(location.latitude, location.longitude),
            "data-api-service-cinema-bylocation.json"
        )
        viewModel.location.value = location
        val loadable = viewModel.items
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Loaded<List<CinemaView>>>(loadable)
        val result = loadable.result
        assertEquals(6, result.size, "Expected to contain 6 elements, but was: $result")
    }

    @Test
    fun items_failGracefully_withoutLocation() = runTest {
        val loadable = viewModel.items
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

    @Test
    fun items_failGracefully_withLocation() = runTest {
        val location = LocationSnapshot(1.0, 2.0)
        viewModel.location.value = location
        responder.onUrlRespond(
            UrlResponder.CinemaLocation(location.latitude, location.longitude),
            "data-api-service-cinema-bylocation.json"
        )
        val loadable = viewModel.items
            .dropWhile { it is Loadable.Loading }
            .first()
        assertIs<Loadable.Error<*>>(loadable)
    }

    // ---

    private fun LocationSnapshot(lat: Double, lng: Double) = object : LocationSnapshot {
        override val latitude: Double = lat
        override val longitude: Double = lng
    }

}