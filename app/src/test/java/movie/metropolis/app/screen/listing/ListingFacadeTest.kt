package movie.metropolis.app.screen.listing

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import kotlin.test.assertEquals

class ListingFacadeTest : FeatureTest() {

    private lateinit var facade: ListingFacade

    override fun prepare() {
        facade = FacadeModule().listing(event)
    }

    @Test
    fun returns_current_success() = runTest {
        responder.onUrlRespond(
            UrlResponder.MoviesByShowing("SHOWING"),
            "data-api-service-films-by-showing-type-SHOWING.json"
        )
        val result = facade.getCurrent()
        assert(result.isSuccess) { result }
        assertEquals(34, result.getOrThrow().size)
    }

    @Test
    fun returns_current_failure() = runTest {
        val result = facade.getCurrent()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_upcoming_success() = runTest {
        responder.onUrlRespond(
            UrlResponder.MoviesByShowing("FUTURE"),
            "data-api-service-films-by-showing-type-FUTURE.json"
        )
        val result = facade.getUpcoming()
        assert(result.isSuccess) { result }
        assertEquals(18, result.getOrThrow().size)
    }

    @Test
    fun returns_upcoming_failure() = runTest {
        val result = facade.getUpcoming()
        assert(result.isFailure) { result }
    }

}