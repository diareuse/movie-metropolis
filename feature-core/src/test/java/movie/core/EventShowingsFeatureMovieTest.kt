package movie.core

import kotlinx.coroutines.test.runTest
import movie.core.db.dao.ShowingDao
import movie.core.db.model.ShowingStored
import movie.core.di.EventFeatureModule
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Movie
import movie.core.nwk.EventService
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.MovieEventResponse
import movie.core.util.thenAnswerSus
import movie.core.util.wheneverSus
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFails

class EventShowingsFeatureMovieTest : FeatureTest() {

    private lateinit var cinema: EventCinemaFeature
    private lateinit var showing: ShowingDao
    private lateinit var service: EventService
    private lateinit var feature: EventShowingsFeature.Movie
    private lateinit var movie: Movie

    override fun prepare() {
        movie = mock {
            on { id }.thenReturn("1")
        }
        cinema = mock {}
        service = mock {}
        showing = mock {}
        feature = EventFeatureModule()
            .showings(showing, mock(), service, mock(), mock(), mock(), cinema)
            .movie(movie, Location(0.0, 0.0))
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        val cinemas = cinema_responds_success()
        val testData = cinemaEvents_responds_success(modifier = {
            val movies = it.movies.map { it.copy(id = "1") }
            val events = it.events.map { it.copy(movieId = "1") }
            it.copy(movies = movies, events = events)
        })
        val result = feature.get(Date()).last().getOrThrow()
        assertEquals(testData.events.size, result.flatMap { it.value }.size)
        assertEquals(cinemas.size, result.size)
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        cinema_responds_success()
        val testEvents = showings_responds_success()
        val result = feature.get(Date()).last().getOrThrow()
        assertEquals(testEvents.size, result.flatMap { it.value }.size)
    }

    @Test
    fun get_throws() = runTest {
        assertFails {
            feature.get(Date()).last().getOrThrow()
        }
    }

    @Test
    fun get_savesData_fromNetwork() = runTest {
        cinema_responds_success()
        val testData = cinemaEvents_responds_success(modifier = {
            val movies = it.movies.map { it.copy(id = "1") }
            val events = it.events.map { it.copy(movieId = "1") }
            it.copy(movies = movies, events = events)
        })
        feature.get(Date()).first().getOrThrow()
        verify(showing, times(testData.events.size)).insertOrUpdate(any())
    }

    // ---

    private fun cinema_responds_success(): List<Cinema> {
        val data = DataPool.Cinemas.all()
        wheneverSus { cinema.get(anyOrNull(), any()) }.thenAnswerSus {
            val callback = getArgument<ResultCallback<Iterable<Cinema>>>(1)
            callback.invoke(Result.success(data))
        }
        return data
    }

    private fun showings_responds_success(): List<ShowingStored> {
        val data = DataPool.ShowingsStored.all()
        wheneverSus { showing.selectByCinema(any(), any(), any(), any()) }.thenReturn(data)
        return data
    }

    private fun cinemaEvents_responds_success(
        modifier: Modifier<MovieEventResponse> = { it }
    ): MovieEventResponse {
        val result = BodyResponse(DataPool.MovieEventResponses.first(modifier))
        wheneverSus { service.getEventsInCinema(any(), any()) }
            .thenReturn(Result.success(result))
        return result.body
    }

    private suspend fun EventShowingsFeature.Movie.get(date: Date): List<Result<CinemaWithShowings>> {
        val milestones = mutableListOf<Result<CinemaWithShowings>>()
        get(date) {
            milestones += it
        }
        return milestones
    }

}