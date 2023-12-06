@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.db.dao.BookingDao
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieMediaDao
import movie.core.db.dao.MoviePreviewDao
import movie.core.db.model.MoviePreviewView
import movie.core.di.EventFeatureModule
import movie.core.nwk.EventService
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.ExtendedMovieResponse
import movie.core.nwk.model.ShowingType
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.core.util.wheneverBlocking
import movie.image.ImageAnalyzer
import movie.image.Swatch
import movie.image.SwatchColor
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atMost
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import java.util.Locale
import kotlin.test.assertContains
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFalse

abstract class EventPreviewFeatureTest {

    protected lateinit var detail: EventDetailFeature
    protected lateinit var sync: SyncPreference
    protected lateinit var booking: BookingDao
    protected lateinit var preference: EventPreference
    protected lateinit var analyzer: ImageAnalyzer
    protected lateinit var media: MovieMediaDao
    protected lateinit var preview: MoviePreviewDao
    protected lateinit var movie: MovieDao
    protected lateinit var service: EventService
    protected lateinit var feature: EventPreviewFeature

    @Before
    fun prepare() {
        service = mock {}
        movie = mock {}
        preview = mock {}
        media = mock {}
        analyzer = mock {
            onBlocking { getColors(any()) }
                .thenReturn(Swatch(SwatchColor(0), SwatchColor(0), SwatchColor(0)))
        }
        preference = mock {
            on { keywords }.thenReturn(emptyList())
        }
        booking = mock {}
        sync = mock {
            on { previewCurrent }.thenReturn(Date())
            on { previewUpcoming }.thenReturn(Date())
        }
        detail = mock {}
        feature = EventFeatureModule().preview(
            service = service,
            movie = movie,
            preview = preview,
            media = media,
            preference = preference,
            booking = booking,
            sync = sync
        ).run(::create)
    }

    abstract fun create(factory: EventPreviewFeature.Factory): EventPreviewFeature

    class Current : EventPreviewFeatureTest() {
        override fun create(factory: EventPreviewFeature.Factory) = factory.current()

        @Test
        fun get_writes_syncDate() = runTest {
            service_responds_success()
            feature.get()
            verify(sync).previewCurrent = any()
        }

        @Test
        fun get_not_writes_syncDate() = runTest {
            database_responds_success()
            feature.get()
            verify(sync, never()).previewCurrent = any()
        }

        @Test
        fun get_returns_fromNetwork_whenInvalidated() = runTest {
            service_responds_success()
            database_responds_success()
            whenever(sync.previewCurrent).thenReturn(Date(0))
            feature.get()
            verify(service).getMoviesByType(ShowingType.Current)
        }

    }

    class Upcoming : EventPreviewFeatureTest() {
        override fun create(factory: EventPreviewFeature.Factory) = factory.upcoming()

        @Test
        fun get_writes_syncDate() = runTest {
            service_responds_success()
            feature.get()
            verify(sync).previewUpcoming = any()
        }

        @Test
        fun get_not_writes_syncDate() = runTest {
            database_responds_success()
            feature.get()
            verify(sync, never()).previewUpcoming = any()
        }

        @Test
        fun get_returns_fromNetwork_whenInvalidated() = runTest {
            service_responds_success()
            database_responds_success()
            whenever(sync.previewUpcoming).thenReturn(Date(0))
            feature.get()
            verify(service).getMoviesByType(ShowingType.Upcoming)
        }

    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        val testData = service_responds_success()
        val output = feature.get()
        assertEquals(testData.size, output.count())
    }

    @Test
    fun get_sorts_fromNetwork() = runTest {
        service_responds_success()
        val output = feature.get()
        assertEquals(listOf("3", "1", "2"), output.map { it.id }.toList())
    }

    @Test
    fun get_stores() = runTest {
        val testData = service_responds_success()
        feature.get()
        verify(preview, atMost(1)).deleteAll(any())
        verify(movie, atMost(testData.size)).insertOrUpdate(any())
        verify(preview, atMost(testData.size)).insertOrUpdate(any())
        verify(media, atMost(testData.flatMap { it.media }.size)).insertOrUpdate(any())
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        val testData = database_responds_success()
        val output = feature.get()
        assertEquals(testData.size, output.count())
    }

    @Test
    fun get_sorts_fromDatabase() = runTest {
        database_responds_success()
        val output = feature.get()
        assertEquals(listOf("3", "1", "2"), output.map { it.id }.toList())
    }

    @Test
    fun get_returns_fromNetwork_whenDatabaseEmpty() = runTest {
        database_responds_empty()
        service_responds_success()
        val output = feature.get()
        assert(output.count() > 0)
    }

    @Test
    fun get_filters_fromNetwork() = runTest {
        service_responds_success()
        val testBooking = booking_responds_positive()
        val output = feature.get()
        assertFalse(
            output.any { it.id in testBooking },
            "Expected output not to contain $testBooking, but was ${output.map { it.id }}"
        )
    }

    @Test
    fun get_filters_fromDatabase() = runTest {
        database_responds_success()
        val testBooking = booking_responds_positive()
        val output = feature.get()
        assertFalse(
            output.any { it.id in testBooking },
            "Expected output not to contain $testBooking, but was ${output.map { it.id }}"
        )
    }

    @Test
    fun get_filtersMovies_fromDatabase() = runTest {
        val expected = database_responds_success()
        onlyMovies_responds_true()
        val output = feature.get().toList().map { it.id }
        for (i in expected)
            assertContains(output, i.id)
    }

    @Test
    fun get_filtersMovies_fromNetwork() = runTest {
        val expected = service_responds_success()
        onlyMovies_responds_true()
        val output = feature.get().toList().map { it.id }
        for (i in expected)
            assertContains(output, i.id.key)
    }

    @Test
    fun get_filtersKeywords_fromNetwork() = runTest {
        service_responds_success {
            it.copy(
                metadata = mapOf(
                    Locale.getDefault() to ExtendedMovieResponse.Metadata("none")
                )
            )
        }
        keywords_responds_value("none")
        val output = feature.get()
        assertContentEquals(emptySequence(), output)
    }

    @Test
    fun get_filtersKeywords_fromDatabase() = runTest {
        database_responds_success {
            it.copy(name = "none")
        }
        keywords_responds_value("none")
        val output = feature.get()
        assertContentEquals(emptySequence(), output)
    }

    // ---

    protected fun onlyMovies_responds_true() {
        whenever(preference.onlyMovies).thenReturn(true)
    }

    protected fun keywords_responds_value(vararg values: String) {
        whenever(preference.keywords).thenReturn(values.toList())
    }

    private fun booking_responds_positive(): List<String> {
        val data = listOf("1")
        wheneverBlocking { booking.selectIds() }.thenReturn(data)
        wheneverBlocking { preference.filterSeen }.thenReturn(true)
        return data
    }

    protected fun database_responds_success(modifier: Modifier<MoviePreviewView> = { it }): List<MoviePreviewView> {
        val data = DataPool.MoviePreviewViews.all(modifier)
        wheneverBlocking { preview.selectCurrent() }.thenReturn(data)
        wheneverBlocking { preview.selectUpcoming() }.thenReturn(data)
        wheneverBlocking { media.select(any()) }.thenReturn(DataPool.MovieMediaViews.all())
        return data
    }

    private fun database_responds_empty() {
        wheneverBlocking { preview.selectCurrent() }.thenReturn(emptyList())
        wheneverBlocking { preview.selectUpcoming() }.thenReturn(emptyList())
    }

    protected fun service_responds_success(modifier: Modifier<ExtendedMovieResponse> = { it }): List<ExtendedMovieResponse> {
        val data = DataPool.ExtendedMovieResponses.all(modifier)
        wheneverBlocking { service.getMoviesByType(any()) }
            .thenReturn(Result.success(BodyResponse(data)))
        return data
    }

}