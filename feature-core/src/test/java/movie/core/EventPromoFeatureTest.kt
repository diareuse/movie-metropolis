@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.db.dao.MoviePromoDao
import movie.core.di.EventFeatureModule
import movie.core.model.Movie
import movie.core.nwk.CinemaService
import movie.core.nwk.EndpointProvider
import movie.core.nwk.model.PromoCardResponse
import movie.core.nwk.model.ResultsResponse
import movie.core.util.awaitChildJobCompletion
import movie.core.util.wheneverBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.test.assertContains

class EventPromoFeatureTest {

    private lateinit var movie: Movie
    private lateinit var feature: (CoroutineScope) -> EventPromoFeature
    private lateinit var provider: EndpointProvider
    private lateinit var service: CinemaService
    private lateinit var dao: MoviePromoDao

    @Before
    fun prepare() {
        provider = mock {
            on { domain }.thenReturn("https://domain.org/")
        }
        service = mock()
        dao = mock()
        feature = { scope -> EventFeatureModule().promo(dao, service, provider, scope) }
        movie = mock {
            on { id }.thenReturn("id")
        }
    }

    @Test
    fun get_returns_fromDatabase() = runTest {
        val subject = database_responds_success()
        val output = feature(this).get(movie)
        assertContains(output.getOrThrow().url, subject)
    }

    @Test
    fun get_returns_fromNetwork() = runTest {
        val testData = network_responds_success()
        val output = feature(this).get(movie)
        assertContains(output.getOrThrow().url, testData.imageId)
    }

    /*@Test // todo move to presentation
    fun get_returnsColor_fromNetwork() = runTest {
        network_responds_success()
        val color = analyzer_responds_success()
        val output = feature(this).get(movie)
        assertEquals(color, output.getOrThrow().spotColor)
    }

    @Test
    fun get_returnsColor_fromDatabase() = runTest {
        database_responds_success()
        val color = analyzer_responds_success()
        val output = feature(this).get(movie)
        assertEquals(color, output.getOrThrow().spotColor)
    }*/

    @Test
    fun get_returnsFullUrl_fromNetwork() = runTest {
        val domain = provider.domain
        network_responds_success()
        val output = feature(this).get(movie)
        assertContains(output.getOrThrow().url, domain)
    }

    @Test
    fun get_returnsFullUrl_fromDatabase() = runTest {
        val domain = provider.domain
        database_responds_success()
        val output = feature(this).get(movie)
        assertContains(output.getOrThrow().url, domain)
    }

    @Test
    fun get_stores() = runTest {
        network_responds_success()
        feature(this).get(movie)
        awaitChildJobCompletion()
        verify(dao, times(1)).insertOrUpdate(any())
    }

    // ---

    /*private fun analyzer_responds_success(): Int {
        val color = nextInt(0xff000000.toInt(), 0xffffffff.toInt())
        val swatch = Swatch(SwatchColor(color), SwatchColor(color), SwatchColor(color))
        wheneverBlocking { analyzer.getColors(any()) }.thenReturn(swatch)
        return color
    }*/

    private fun network_responds_success(): PromoCardResponse {
        val data = DataPool.PromoCardResponses.first().let(::listOf)
        wheneverBlocking { service.getPromoCards() }.thenReturn(Result.success(ResultsResponse(data)))
        return data.first()
    }

    private fun database_responds_success(subject: String = "some-url-fragment"): String {
        val id = movie.id
        wheneverBlocking { dao.select(id) }.thenReturn(subject)
        return subject
    }

}