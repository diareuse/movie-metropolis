package movie.metropolis.app.screen.detail

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import java.util.Date
import kotlin.test.assertEquals

class MovieFacadeTest : FeatureTest() {

    private lateinit var facade: MovieFacade

    override fun prepare() {
        facade = FacadeModule().movie(event).create("5376O2R")
    }

    @Test
    fun returns_availableFrom_success() = runTest {
        prepareSuccessDetail()
        val result = facade.getAvailableFrom()
        assert(result.isSuccess) { result }
        assertEquals(Date(1666821600000), result.getOrThrow())
    }

    @Test
    fun returns_availableFrom_failure() = runTest {
        val result = facade.getAvailableFrom()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_movie_success() = runTest {
        prepareSuccessDetail()
        val result = facade.getMovie()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_movie_failure() = runTest {
        val result = facade.getMovie()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_poster_success() = runTest {
        prepareSuccessDetail()
        val result = facade.getPoster()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_poster_failure() = runTest {
        val result = facade.getPoster()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_trailer_success() = runTest {
        prepareSuccessDetail()
        val result = facade.getTrailer()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_trailer_failure() = runTest {
        val result = facade.getTrailer()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_showings_success() = runTest {
        prepareSuccessDetail()
        prepareSuccessCinema()
        prepareSuccessEvent()
        val result = facade.getShowings(Date(0), 0.0, 0.0)
        assert(result.isSuccess) { result }
        assertEquals(6, result.getOrThrow().size)
    }

    @Test
    fun returns_showings_failure() = runTest {
        val result = facade.getShowings(Date(0), 0.0, 0.0)
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_showings_withAllCinemas_ifLocationNull() = runTest {
        prepareSuccessDetail()
        prepareSuccessCinemaWithoutLocation()
        prepareSuccessEvent()
        val result = facade.getShowings(Date(0), 0.0, 0.0)
        assert(result.isSuccess) { result }
        assertEquals(13, result.getOrThrow().size)
    }

    // ---

    private fun prepareSuccessDetail() {
        responder.onUrlRespond(
            UrlResponder.Detail(),
            "data-api-service-films-byDistributorCode.json"
        )
    }

    private fun prepareSuccessCinema() {
        responder.onUrlRespond(
            UrlResponder.CinemaLocation(0.0, 0.0),
            "data-api-service-cinema-bylocation.json"
        )
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
    }

    private fun prepareSuccessCinemaWithoutLocation() {
        responder.onUrlRespond(UrlResponder.CinemaLocation(0.0, 0.0), "")
        responder.onUrlRespond(UrlResponder.Cinema, "cinemas.json")
    }

    private fun prepareSuccessEvent() {
        val ids = listOf(
            "1056",
            "1052",
            "1033",
            "1031",
            "1030",
            "1051",
            "1037",
            "1055",
            "1054",
            "1034",
            "1053",
            "1036",
            "1035"
        )
        for (id in ids) responder.onUrlRespond(
            UrlResponder.EventOccurrence(id, "1970-01-01"),
            "quickbook-film-events-in-cinema-at-date.json"
        )
    }

}