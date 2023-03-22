@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.di.EventFeatureModule
import movie.core.model.Movie
import movie.core.nwk.EventService
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.MovieDetailResponse
import movie.core.nwk.model.MovieDetailsResponse
import movie.core.util.awaitChildJobCompletion
import movie.core.util.wheneverBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

class EventDetailFeatureTest {

    private lateinit var item: Movie
    private lateinit var feature: (CoroutineScope) -> EventDetailFeature
    private lateinit var media: MovieMediaDao
    private lateinit var detail: MovieDetailDao
    private lateinit var movie: MovieDao
    private lateinit var service: EventService

    @Before
    fun prepare() {
        media = mock {}
        detail = mock {}
        movie = mock {}
        service = mock {}
        feature = { scope ->
            EventFeatureModule()
                .detail(service, movie, detail, media, scope)
        }
        item = mock {
            on { id }.thenReturn("")
        }
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        service_responds_success()
        val output = feature(this).get(item)
        output.getOrThrow()
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        database_responds_success()
        val output = feature(this).get(item)
        output.getOrThrow()
    }

    @Test
    fun get_stores() = runTest {
        val testData = service_responds_success()
        feature(this).get(item)
        awaitChildJobCompletion()
        verify(movie, times(1)).insertOrUpdate(any())
        verify(detail, times(1)).insertOrUpdate(any())
        verify(media, times(testData.media.size)).insertOrUpdate(any())
    }

    // ---

    private fun database_responds_success() {
        val detailView = DataPool.MovieDetailViews.first()
        val mediaViews = DataPool.MovieMediaViews.all()
        wheneverBlocking { detail.select(any()) }.thenReturn(detailView)
        wheneverBlocking { media.select(any()) }.thenReturn(mediaViews)
    }

    private fun service_responds_success(): MovieDetailResponse {
        val data = DataPool.MovieDetailResponses.first()
        wheneverBlocking { service.getDetail(any()) }
            .thenReturn(Result.success(BodyResponse(MovieDetailsResponse(data))))
        return data
    }

}