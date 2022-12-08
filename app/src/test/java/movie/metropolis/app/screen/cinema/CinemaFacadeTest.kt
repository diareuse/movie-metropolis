package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import java.util.Date

class CinemaFacadeTest : FeatureTest() {

    private lateinit var facade: CinemaFacade

    override fun prepare() {
        facade = FacadeModule().cinema(event).create("1031")
    }

    @Test
    fun returns_cinema_withIdenticalId() = runTest {
        prepareValidCinemaResponse()
        val result = facade.getCinema()
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().id == "1031") { result }
    }

    @Test
    fun returns_failure_ifNotFound() = runTest {
        val result = facade.getCinema()
        assert(result.isFailure)
    }

    @Test
    fun returns_showings_ifCinemaExists() = runTest {
        prepareValidResponse()
        val result = facade.getShowings(Date(0))
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().size == 16) { "Expected 16, but was ${result.getOrThrow().size}" }
    }

    @Test
    fun returns_failure_ifCinemaNotFound() = runTest {
        prepareValidEventResponse()
        val result = facade.getShowings(Date(0))
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_sortedShowings() = runTest {
        prepareValidResponse()
        val result = facade.getShowings(Date(0))
        assert(result.isSuccess)
        var previousSize = Int.MAX_VALUE
        for (item in result.getOrThrow())
            assert(previousSize >= item.availability.size) {
                "Showings are not ordered correctly, previous item ($previousSize) has less items than this (${item.availability.size})"
            }.also {
                previousSize = item.availability.size
            }
    }

    // ---

    private fun prepareValidCinemaResponse() =
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")

    private fun prepareValidEventResponse() =
        responder.onUrlRespond(
            UrlResponder.EventOccurrence("1031", "1970-01-01"),
            "quickbook-film-events-in-cinema-at-date.json"
        )

    private fun prepareValidResponse() {
        prepareValidCinemaResponse()
        prepareValidEventResponse()
    }

}