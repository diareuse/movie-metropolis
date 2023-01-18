package movie.core

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.db.dao.BookingDao
import movie.core.db.dao.MovieReferenceDao
import movie.core.db.dao.ShowingDao
import movie.core.db.model.MovieReferenceView
import movie.core.db.model.ShowingStored
import movie.core.di.EventFeatureModule
import movie.core.model.Cinema
import movie.core.nwk.EventService
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.MovieEventResponse
import movie.core.preference.EventPreference
import org.junit.Test
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class EventShowingsFeatureCinemaTest : FeatureTest() {

    private lateinit var booking: BookingDao
    private lateinit var preference: EventPreference
    private lateinit var reference: MovieReferenceDao
    private lateinit var showing: ShowingDao
    private lateinit var service: EventService
    private lateinit var feature: EventShowingsFeature.Cinema
    private lateinit var cinema: Cinema

    override fun prepare() {
        cinema = mock {
            on { id }.thenReturn("id")
        }
        service = mock {}
        showing = mock {}
        reference = mock {}
        booking = mock {}
        preference = mock {
            on { filterSeen }.thenReturn(false)
        }
        feature = EventFeatureModule()
            .showings(showing, reference, service, preference, booking)
            .cinema(cinema)
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        val testData = cinemaEvents_responds_success()
        val result = feature.get(Date()).last().getOrThrow()
        assertEquals(testData.events.size, result.flatMap { it.value }.size)
        assertEquals(testData.movies.size, result.size)
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        val testMovies = references_responds_success()
        val testEvents = showings_responds_success()
        val result = feature.get(Date()).last().getOrThrow()
        assertEquals(testEvents.size, result.flatMap { it.value }.size)
        for (movie in result.keys)
            assertEquals(testMovies.id, movie.id)
    }

    @Test
    fun get_throws() = runTest {
        assertFails {
            feature.get(Date()).last().getOrThrow()
        }
    }

    @Test
    fun get_returns_filteredOutResults_fromNetwork() = runTest {
        val booked = booking_responds("1")
        preferences_responds_positive()
        cinemaEvents_responds_success(modifier = {
            val movies = it.movies.mapIndexed { index, it -> it.copy(id = "$index") }
            it.copy(movies = movies)
        })
        val results = feature.get(Date()).map { it.getOrThrow() }
        for (result in results)
            assertTrue("Expected to not contain booked values") {
                result.keys.none { it.id in booked }
            }
    }

    @Test
    fun get_returns_filteredOutResults_fromDatabase() = runTest {
        val booked = booking_responds("1")
        preferences_responds_positive()
        references_responds_success(modifier = {
            it.copy(id = "1")
        })
        showings_responds_success()
        val results = feature.get(Date()).map { it.getOrThrow() }
        for (result in results)
            assertTrue("Expected to not contain booked values") {
                result.keys.none { it.id in booked }
            }
    }

    @Test
    fun get_returns_sorted_values_fromNetwork() = runTest {
        cinemaEvents_responds_success()
        val result = feature.get(Date()).first().getOrThrow()
        var last = Int.MAX_VALUE
        for ((_, value) in result) {
            assertTrue(
                last >= value.count(),
                "Expected last($last) to be larger or equal to ${value.count()}"
            )
            last = value.count()
        }
    }

    @Test
    fun get_returns_sorted_values_fromDatabase() = runTest {
        references_responds_success()
        showings_responds_success()
        val result = feature.get(Date()).first().getOrThrow()
        var last = Int.MAX_VALUE
        for ((_, value) in result) {
            assertTrue(
                last >= value.count(),
                "Expected last($last) to be larger or equal to ${value.count()}"
            )
            last = value.count()
        }
    }

    // ---

    private fun preferences_responds_positive() {
        whenever(preference.filterSeen).thenReturn(true)
    }

    private fun booking_responds(vararg ids: String): List<String> {
        wheneverSus { booking.selectIds() }.thenReturn(ids.toList())
        return ids.toList()
    }

    private fun showings_responds_success(): List<ShowingStored> {
        val data = DataPool.ShowingsStored.all()
        val id = cinema.id
        wheneverSus { showing.selectByCinema(any(), any(), eq(id)) }.thenReturn(data)
        return data
    }

    private fun references_responds_success(
        modifier: Modifier<MovieReferenceView> = { it }
    ): MovieReferenceView {
        val data = DataPool.MovieReferenceViews.first(modifier)
        wheneverSus { reference.select(any()) }.thenReturn(data)
        return data
    }

    private fun cinemaEvents_responds_success(
        modifier: Modifier<MovieEventResponse> = { it }
    ): MovieEventResponse {
        val result = BodyResponse(DataPool.MovieEventResponses.first(modifier))
        val id = cinema.id
        wheneverSus { service.getEventsInCinema(eq(id), any()) }
            .thenReturn(Result.success(result))
        return result.body
    }

    private suspend fun EventShowingsFeature.Cinema.get(date: Date): List<Result<MovieWithShowings>> {
        val milestones = mutableListOf<Result<MovieWithShowings>>()
        get(date) {
            milestones += it
        }
        return milestones
    }

}

inline fun <T> wheneverSus(crossinline body: suspend () -> T) = whenever(runBlocking { body() })
inline fun <T, R> KStubbing<T>.onSus(crossinline body: suspend T.() -> R) =
    on { runBlocking { body() } }