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
import movie.core.model.MoviePreview
import movie.core.nwk.EventService
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.ExtendedMovieResponse
import movie.core.preference.EventPreference
import movie.core.preference.SyncPreference
import movie.core.util.wheneverSus
import movie.image.ImageAnalyzer
import movie.image.Swatch
import movie.image.SwatchColor
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import java.util.Date
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class EventPreviewFeatureTest {

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
        preference = mock {}
        booking = mock {}
        sync = mock {
            on { previewCurrent }.thenReturn(Date())
            on { previewUpcoming }.thenReturn(Date())
        }
        feature = EventFeatureModule().preview(
            service = service,
            movie = movie,
            preview = preview,
            media = media,
            analyzer = analyzer,
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

    }

    class Upcoming : EventPreviewFeatureTest() {
        override fun create(factory: EventPreviewFeature.Factory) = factory.upcoming()

        @Test
        fun get_writes_syncDate() = runTest {
            service_responds_success()
            feature.get()
            verify(sync).previewUpcoming = any()
        }

    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        val testData = service_responds_success()
        val outputs = feature.get()
        for (output in outputs)
            assertEquals(testData.size, output.getOrThrow().size)
    }

    @Test
    fun get_sorts_fromNetwork() = runTest {
        service_responds_success()
        val outputs = feature.get()
        for (output in outputs)
            assertEquals(listOf("1", "2", "3"), output.getOrThrow().map { it.id })
    }

    @Test
    fun get_stores() = runTest {
        val testData = service_responds_success()
        feature.get()
        verify(preview, times(1)).deleteAll(any())
        verify(movie, times(testData.size)).insertOrUpdate(any())
        verify(preview, times(testData.size)).insertOrUpdate(any())
        verify(media, times(testData.flatMap { it.media }.size)).insertOrUpdate(any())
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        val testData = database_responds_success()
        val outputs = feature.get()
        for (output in outputs)
            assertEquals(testData.size, output.getOrThrow().size)
    }

    @Test
    fun get_sorts_fromDatabase() = runTest {
        database_responds_success()
        val outputs = feature.get()
        for (output in outputs)
            assertEquals(listOf("1", "2", "3"), output.getOrThrow().map { it.id })
    }

    @Test
    fun get_returns_fromNetwork_whenDatabaseEmpty() = runTest {
        database_responds_empty()
        service_responds_success()
        for (output in feature.get())
            output.getOrThrow()
    }

    @Test
    fun get_filters_fromNetwork() = runTest {
        service_responds_success()
        val testBooking = booking_responds_positive()
        for (output in feature.get().map { it.getOrThrow() })
            assertFalse(
                output.any { it.id in testBooking },
                "Expected output not to contain $testBooking, but was ${output.map { it.id }}"
            )
    }

    @Test
    fun get_filters_fromDatabase() = runTest {
        database_responds_success()
        val testBooking = booking_responds_positive()
        for (output in feature.get().map { it.getOrThrow() })
            assertFalse(
                output.any { it.id in testBooking },
                "Expected output not to contain $testBooking, but was ${output.map { it.id }}"
            )
    }

    @Test
    fun get_fetches_colors_fromNetwork() = runTest {
        service_responds_success()
        val color = analyzer_responds_success()
        val output = feature.get().last().getOrThrow()
        assertTrue(
            output.all { it.spotColor == color },
            "Expected output to contain color $color, but was ${output.map { it.spotColor }}"
        )
    }

    @Test
    fun get_fetches_colors_fromDatabase() = runTest {
        database_responds_success()
        val color = analyzer_responds_success()
        val output = feature.get().last().getOrThrow()
        assertTrue(
            output.all { it.spotColor == color },
            "Expected output to contain color $color, but was ${output.map { it.spotColor }}"
        )
    }

    // ---

    private fun analyzer_responds_success(): Int {
        val color = nextInt(0xff000000.toInt(), 0xffffffff.toInt())
        val swatch = Swatch(SwatchColor(color), SwatchColor(color), SwatchColor(color))
        wheneverSus { analyzer.getColors(any()) }.thenReturn(swatch)
        return color
    }

    private fun booking_responds_positive(): List<String> {
        val data = listOf("1")
        wheneverSus { booking.selectIds() }.thenReturn(data)
        wheneverSus { preference.filterSeen }.thenReturn(true)
        return data
    }

    private fun database_responds_success(): List<MoviePreviewView> {
        val data = DataPool.MoviePreviewViews.all()
        wheneverSus { preview.selectCurrent() }.thenReturn(data)
        wheneverSus { preview.selectUpcoming() }.thenReturn(data)
        wheneverSus { media.select(any()) }.thenReturn(DataPool.MovieMediaViews.all())
        return data
    }

    private fun database_responds_empty() {
        wheneverSus { preview.selectCurrent() }.thenReturn(emptyList())
        wheneverSus { preview.selectUpcoming() }.thenReturn(emptyList())
    }

    protected fun service_responds_success(): List<ExtendedMovieResponse> {
        val data = DataPool.ExtendedMovieResponses.all()
        wheneverSus { service.getMoviesByType(any()) }.thenReturn(Result.success(BodyResponse(data)))
        return data
    }

    protected suspend fun EventPreviewFeature.get(): List<Result<List<MoviePreview>>> {
        val results = mutableListOf<Result<List<MoviePreview>>>()
        get { results += it }
        return results
    }

}