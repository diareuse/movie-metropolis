@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.db.dao.CinemaDao
import movie.core.db.model.CinemaStored
import movie.core.di.EventFeatureModule
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.nwk.CinemaService
import movie.core.nwk.model.CinemaResponse
import movie.core.nwk.model.ResultsResponse
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.core.util.wheneverBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Date
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails

class EventCinemaFeatureTest {

    private lateinit var sync: SyncPreference
    private lateinit var preference: EventPreference
    private lateinit var cinema: CinemaDao
    private lateinit var service: CinemaService
    private lateinit var feature: EventCinemaFeature

    @Before
    fun prepare() {
        service = mock {}
        cinema = mock {}
        preference = mock {
            on { distanceKms }.thenReturn(100)
        }
        sync = mock {
            on { previewCurrent }.thenReturn(Date())
            on { previewUpcoming }.thenReturn(Date())
        }
        feature = EventFeatureModule().cinema(service, cinema, preference, sync)
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        val testData = service_responds_success()
        val outputs = feature.get(null).map { it.getOrThrow() }
        for (output in outputs)
            assertEquals(testData.size, output.count())
    }

    @Test
    fun get_returns_fromNetwork_withLocation() = runTest {
        val testData = service_responds_success(modifier = {
            it.copy(latitude = 0.0, longitude = 0.0)
        })
        val outputs = feature.get(Location(0.0, 0.0)).map { it.getOrThrow() }
        for (output in outputs)
            assertEquals(testData.size, output.count())
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        val testData = database_responds_success()
        val outputs = feature.get(null).map { it.getOrThrow() }
        for (output in outputs)
            assertEquals(testData.size, output.count())
    }

    @Test
    fun get_returns_fromDatabase_withLocation() = runTest {
        val testData = database_responds_success(modifier = {
            it.copy(latitude = 0.0, longitude = 0.0)
        })
        val outputs = feature.get(Location(0.0, 0.0)).map { it.getOrThrow() }
        for (output in outputs)
            assertEquals(testData.size, output.count())
    }

    @Test
    fun get_returns_sorted_fromDatabase() = runTest {
        database_responds_success()
        val outputs = feature.get(null).map { it.getOrThrow() }
        for (output in outputs)
            assertContentEquals(listOf("a", "b", "c", "d"), output.map { it.city })
    }

    @Test
    fun get_returns_sortedByDistance_fromDatabase() = runTest {
        database_responds_success()
        val outputs = feature.get(Location(0.0, 0.0)).map { it.getOrThrow() }
        for (output in outputs)
            assertContentEquals(listOf("1", "2", "3"), output.map { it.id })
    }

    @Test
    fun get_returns_sorted_fromNetwork() = runTest {
        service_responds_success()
        val outputs = feature.get(null).map { it.getOrThrow() }
        for (output in outputs)
            assertContentEquals(listOf("a", "b", "c", "d"), output.map { it.city })
    }

    @Test
    fun get_returns_sortedByDistance_fromNetwork() = runTest {
        service_responds_success()
        val outputs = feature.get(Location(0.0, 0.0)).map { it.getOrThrow() }
        for (output in outputs)
            assertContentEquals(listOf("1", "2", "3"), output.map { it.id })
    }

    @Test
    fun get_returns_closestOnly_fromDatabase() = runTest {
        // 1.0,1.0 is roughly 1100kms from 0.0,0.0
        database_responds_success { it.copy(latitude = 1.0, longitude = 1.0) }
        val outputs = feature.get(Location(0.0, 0.0)).map { it.getOrThrow() }
        for (output in outputs)
            assertEquals(emptyList(), output)
    }

    @Test
    fun get_returns_closestOnly_fromNetwork() = runTest {
        // 1.0,1.0 is roughly 1100kms from 0.0,0.0
        service_responds_success { it.copy(latitude = 1.0, longitude = 1.0) }
        val outputs = feature.get(Location(0.0, 0.0)).map { it.getOrThrow() }
        for (output in outputs)
            assertEquals(emptyList(), output)
    }

    @Test
    fun get_stores() = runTest {
        val testData = service_responds_success()
        feature.get(null)
        verify(cinema, times(testData.size)).insertOrUpdate(any())
    }

    @Test
    fun get_throws() = runTest {
        for (output in feature.get(null))
            assertFails { output.getOrThrow() }
    }

    @Test
    fun get_writes_syncDate() = runTest {
        service_responds_success()
        feature.get(null)
        verify(sync).cinema = any()
    }

    // ---

    private fun database_responds_success(
        modifier: Modifier<CinemaStored> = { it }
    ): List<CinemaStored> {
        val data = DataPool.CinemasStored.all(modifier = modifier)
        wheneverBlocking { cinema.selectAll() }.thenReturn(data)
        return data
    }

    private fun service_responds_success(
        modifier: Modifier<CinemaResponse> = { it }
    ): List<CinemaResponse> {
        val data = DataPool.CinemaResponses.all(modifier = modifier)
        wheneverBlocking { service.getCinemas() }.thenReturn(Result.success(ResultsResponse(data)))
        return data
    }

    private suspend fun EventCinemaFeature.get(location: Location?): List<Result<Iterable<Cinema>>> {
        val outputs = mutableListOf<Result<Iterable<Cinema>>>()
        get(location) { outputs += it }
        return outputs
    }

}