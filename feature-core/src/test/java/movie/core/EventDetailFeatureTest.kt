@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieDetailDao
import movie.core.db.dao.MovieMediaDao
import movie.core.di.EventFeatureModule
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.nwk.EventService
import movie.core.nwk.model.BodyResponse
import movie.core.nwk.model.MovieDetailResponse
import movie.core.nwk.model.MovieDetailsResponse
import movie.core.util.wheneverBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.atMost
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

class EventDetailFeatureTest {

    private lateinit var item: Movie
    private lateinit var feature: EventDetailFeature
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
        feature = EventFeatureModule()
            .detail(service, movie, detail, media)
        item = mock {
            on { id }.thenReturn("")
        }
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        service_responds_success()
        val outputs = feature.get(item)
        for (output in outputs)
            output.getOrThrow()
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        database_responds_success()
        val outputs = feature.get(item)
        for (output in outputs)
            output.getOrThrow()
    }

    /*@Test // fixme move to presentation
    fun get_returns_ratingFromNetwork() = runTest {
        service_responds_success()
        val rating = rating_responds_success()
        val output = feature.get(item).last().getOrThrow()
        assertEquals(rating.rating, output.rating)
        assertEquals(rating.linkCsfd, output.linkCsfd)
        assertEquals(rating.linkImdb, output.linkImdb)
        assertEquals(rating.linkRottenTomatoes, output.linkRottenTomatoes)
    }*/

    @Test
    fun get_stores() = runTest {
        val testData = service_responds_success()
        feature.get(item)
        verify(movie, atMost(1)).insertOrUpdate(any())
        verify(detail, atMost(1)).insertOrUpdate(any())
        verify(media, atMost(testData.media.size)).insertOrUpdate(any())
    }

    /*@Test // fixme move to presentation test
    fun get_returns_spotColor_fromNetwork() = runTest {
        service_responds_success()
        val color = analyzer_responds_success()
        val output = feature.get(item).last().getOrThrow()
        assertEquals(color, output.spotColor)
    }

    @Test
    fun get_returns_spotColor_fromDatabase() = runTest {
        database_responds_success()
        val color = analyzer_responds_success()
        val output = feature.get(item).last().getOrThrow()
        assertEquals(color, output.spotColor)
    }*/

    /*@Test // fixme move to presentation
    fun get_pollsNetwork_whenNotRecent() = runTest {
        database_responds_success()
        database_rating_responds_failure()
        feature.get(item)
        verify(rating, atLeastOnce()).get(anyVararg())
    }*/

    // ---

    /*private fun analyzer_responds_success(): Int { // fixme move to presentation tests
        val color = nextInt(0xff000000.toInt(), 0xffffffff.toInt())
        val swatch = Swatch(SwatchColor(color), SwatchColor(color), SwatchColor(color))
        wheneverBlocking { analyzer.getColors(any()) }.thenReturn(swatch)
        return color
    }*/

    /*private fun rating_responds_success(): MovieRatingStored {// fixme move to presentation
        val data = MovieRatingStored("", nextInt(0, 100).toByte(), "imdb", "rtt", "csfd")
        val composed = mock<ComposedRating> {
            on { imdb }.thenReturn(AvailableRating(data.rating, data.linkImdb!!))
            on { csfd }.thenReturn(AvailableRating(data.rating, data.linkCsfd!!))
            on { rottenTomatoes }
                .thenReturn(AvailableRating(data.rating, data.linkRottenTomatoes!!))
            on { max }.thenReturn(AvailableRating(data.rating, data.linkImdb!!))
        }
        wheneverBlocking { rating.get(anyVararg()) }.thenReturn(composed)
        return data
    }*/

    private fun database_responds_success() {
        val detailView = DataPool.MovieDetailViews.first()
        val mediaViews = DataPool.MovieMediaViews.all()
        wheneverBlocking { detail.select(any()) }.thenReturn(detailView)
        wheneverBlocking { media.select(any()) }.thenReturn(mediaViews)
        //wheneverBlocking { ratings.isRecent(any()) }.thenReturn(true)
    }

    /*private fun database_rating_responds_failure() {// fixme move to presentation
        wheneverBlocking { ratings.isRecent(any()) }.thenReturn(false)
    }*/

    private fun service_responds_success(): MovieDetailResponse {
        val data = DataPool.MovieDetailResponses.first()
        wheneverBlocking { service.getDetail(any()) }
            .thenReturn(Result.success(BodyResponse(MovieDetailsResponse(data))))
        return data
    }

    private suspend fun EventDetailFeature.get(movie: Movie): List<Result<MovieDetail>> {
        val outputs = mutableListOf<Result<MovieDetail>>()
        get(movie) {
            outputs += it
        }
        return outputs
    }

}