package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.feature.global.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.getOrThrow
import org.junit.Test

class CinemasFacadeTest : FeatureTest() {

    private lateinit var facade: CinemasFacade

    override fun prepare() {
        facade = FacadeModule().cinemas(event)
    }

    @Test
    fun returns_listOfAll_withoutLocation() = runTest {
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        val result = facade.getCinemas(null, null)
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().size == 13) { "Expected to contain 13 elements. ${result.getOrThrow()}" }
    }

    @Test
    fun returns_listOfLimited_withLocation() = runTest {
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
        responder.onUrlRespond(
            UrlResponder.CinemaLocation(1.0, 1.0),
            "data-api-service-cinema-bylocation.json"
        )
        val result = facade.getCinemas(1.0, 1.0)
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().size == 6) { "Expected to contain 6 elements. ${result.getOrThrow()}" }
    }

    @Test
    fun returns_failure() = runTest {
        val result = facade.getCinemas(1.0, 1.0)
        assert(result.isFailure) { result }
    }

}