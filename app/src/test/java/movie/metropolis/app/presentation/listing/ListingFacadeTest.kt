package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import movie.core.EventPreviewFeature
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.model.MoviePromoPoster
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.testFlow
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import java.util.Date
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

abstract class ListingFacadeTest : FeatureTest() {

    private lateinit var previewFork: EventPreviewFeature
    lateinit var facade: ListingFacade

    abstract fun choose(factory: ListingFacade.Factory): ListingFacade
    abstract fun choose(factory: EventPreviewFeature.Factory): EventPreviewFeature

    override fun prepare() {
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(false)
        facade = FacadeModule()
            .listing(preview, favorite, promo, rating, detail, prefs)
            .run(::choose)
        previewFork = choose(preview)
    }

    class Upcoming : ListingFacadeTest() {
        override fun choose(factory: ListingFacade.Factory) = factory.upcoming()
        override fun choose(factory: EventPreviewFeature.Factory) = factory.upcoming()
    }

    class Current : ListingFacadeTest() {
        override fun choose(factory: ListingFacade.Factory) = factory.current()
        override fun choose(factory: EventPreviewFeature.Factory) = factory.current()
    }

    @Test
    fun toggle_notifies() = runTest {
        preview_responds_success()
        favorite_responds_success()
        val output = testFlow(facade.get())
        advanceUntilIdle()
        val count = output.size
        facade.toggle(mock {
            on { getBase() }.thenReturn(mock())
        })
        advanceUntilIdle()
        assertNotEquals(count, output.size)
    }

    @Test
    fun toggle_calls_feature() = runTest {
        facade.toggle(mock {
            on { getBase() }.thenReturn(mock())
        })
        verify(favorite).toggle(any())
    }

    @Test
    fun promotions_returns_favorite() = runTest {
        preview_responds_success()
        val value = favorite_responds_success()
        val output = testFlow(facade.get())
        advanceUntilIdle()
        for (output in output.last().items)
            assertEquals(value, output.favorite)
        for (output in output.last().promotions)
            assertEquals(value, output.favorite)
    }

    @Test
    fun promotions_returns_setOfAtMost3() = runTest {
        val count = nextInt(0, 100)
        preview_responds_success(count)
        promo_responds_success()
        favorite_responds_success()
        detail_responds_success()
        val output = testFlow(facade.get())
        advanceUntilIdle()
        assertNotEquals(emptyList(), output)
        for (output in output)
            assertTrue {
                output.promotions.size in 0..3
            }
    }

    @Test
    fun promotions_returns_withPosterUrls() = runTest {
        detail_responds_success()
        preview_responds_success()
        val url = promo_responds_success().url
        val outputs = testFlow(facade.get())
        advanceUntilIdle()
        for (output in outputs)
            for (output in output.promotions)
                assertEquals(url, output.poster?.url)
    }

    @Test
    fun promotions_returns_withPosterRatio() = runTest {
        //detail_responds_success()
        preview_responds_success()
        promo_responds_success()
        val outputs = testFlow(facade.get())
        advanceUntilIdle()
        for (output in outputs)
            for (output in output.promotions)
                assertEquals(1.5f, output.poster?.aspectRatio)
    }

    @Test
    fun get_fails() = runTest {
        preview_responds_failure()
        assertFails {
            facade.get().collect()
        }
    }

    @Test
    fun groups_returns_values() = runTest {
        detail_responds_success()
        val values = preview_responds_success {
            on { genres }.thenReturn(emptyList())
        }
        val outputs = testFlow(facade.get())
        advanceUntilIdle()
        for (output in outputs)
            assertContentEquals(values.map { it.id }, output.items.map { it.id })
    }

    // ---

    fun detail_responds_success(
        modifier: KStubbing<MovieDetail>.() -> Unit = {
            on { releasedAt }.thenReturn(Date())
            on { originalName }.thenReturn("original")
            on { name }.thenReturn("name")
        }
    ) {
        val movie = mock { modifier() }
        wheneverBlocking { detail.get(any()) }.thenReturn(movie)
    }

    fun favorite_responds_success(): Boolean {
        val value = true
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(value)
        wheneverBlocking { favorite.toggle(any()) }.thenReturn(value)
        return value
    }

    fun promo_responds_success(): MoviePromoPoster {
        val poster = mock<MoviePromoPoster> {
            on { url }.thenReturn("https://examp.le/image/${nextInt()}")
        }
        wheneverBlocking { promo.get(any()) }.thenReturn(Result.success(poster))
        return poster
    }

    fun preview_responds_success(
        count: Int = 10,
        modifier: KStubbing<MoviePreview>.(Int) -> Unit = {}
    ): List<MoviePreview> {
        val content = List(count) { index ->
            @Suppress("RemoveExplicitTypeArguments")
            mock<MoviePreview> {
                on { id }.thenReturn("id")
                modifier(index)
            }
        }
        wheneverBlocking { previewFork.get() }.thenReturn(content.asSequence())
        return content
    }

    private fun preview_responds_failure() {
        wheneverBlocking { previewFork.get() }.thenThrow(RuntimeException())
    }

}