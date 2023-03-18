package movie.metropolis.app.presentation.listing

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.test.runTest
import movie.core.EventPreviewFeature
import movie.core.model.MoviePreview
import movie.core.model.MoviePromoPoster
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import kotlin.random.Random.Default.nextBoolean
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

abstract class ListingFacadeTest : FeatureTest() {

    private lateinit var previewFork: EventPreviewFeature
    private lateinit var facade: ListingFacade

    abstract fun choose(factory: ListingFacade.Factory): ListingFacade
    abstract fun choose(factory: EventPreviewFeature.Factory): EventPreviewFeature

    override fun prepare() {
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(Result.success(false))
        facade = FacadeModule().listing(preview, favorite, promo).run(::choose)
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
    fun promotions_returns_setOfAtMost3() = runTest {
        val count = nextInt(0, 100)
        preview_responds_success(count)
        for (action in facade.get())
            for (output in action.getOrThrow().promotions())
                assertTrue {
                    output.getOrThrow().size in 0..3
                }
    }

    @Test
    fun groupUp_returns_moviesInGenres() = runTest {
        val count = nextInt(0, 100)
        preview_responds_success(count) {
            on { genres }.thenReturn(listOf("1", "2"))
        }
        for (action in facade.get())
            for (output in action.getOrThrow().groupUp())
                assertContentEquals(
                    setOf(Genre("1"), Genre("2")),
                    output.getOrThrow().keys.asIterable()
                )
    }

    @Test
    fun promotions_returns_withPosterUrls() = runTest {
        preview_responds_success()
        val url = promo_responds_success().url
        val outputs = facade
            .get().last().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs)
            assertEquals(url, output.poster?.url)
    }

    /*@Test // fixme this should take a color from analyzer
    fun promotions_returns_withPosterColors() = runTest {
        preview_responds_success()
        val color = promo_responds_success().spotColor
        val outputs = facade
            .get().last().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs)
            assertEquals(Color(color), output.poster?.spotColor)
    }*/

    @Test
    fun promotions_returns_withPosterRatio() = runTest {
        preview_responds_success()
        promo_responds_success()
        val outputs = facade
            .get().last().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs)
            assertEquals(1.5f, output.poster?.aspectRatio)
    }

    @Test
    fun get_fails() = runTest {
        preview_responds_failure()
        for (output in facade.get())
            assertFails {
                output.getOrThrow()
            }
    }

    @Test
    fun promotions_returns_favorite() = runTest {
        preview_responds_success()
        val value = favorite_responds_success()
        val outputs = facade
            .get().last().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs)
            assertEquals(value, output.favorite)
    }

    @Test
    fun groups_returns_defaultGroup() = runTest {
        preview_responds_success {
            on { genres }.thenReturn(emptyList())
        }
        for (action in facade.get())
            for (output in action.getOrThrow().groupUp())
                assertContentEquals(
                    setOf(Genre("other")),
                    output.getOrThrow().keys.asIterable()
                )
    }

    @Test
    fun toggle_notifies() = runTest {
        favorite_responds_success()
        var notified = false
        facade.addListener { notified = true }
        facade.toggle(mock {
            on { getBase() }.thenReturn(mock())
        })
        assertTrue(notified)
    }

    @Test
    fun toggle_doesNotNotify_whenRemoved() = runTest {
        favorite_responds_success()
        var notified = false
        val listener = facade.addListener { notified = true }
        facade.removeListener(listener)
        facade.toggle(mock {
            on { getBase() }.thenReturn(mock())
        })
        assertFalse(notified)
    }

    @Test
    fun toggle_calls_feature() = runTest {
        facade.toggle(mock {
            on { getBase() }.thenReturn(mock())
        })
        verify(favorite).toggle(any())
    }

    @Test
    fun promotions_return_noRegularPosters() = runTest {
        val testData = preview_responds_success()
        val outputs = facade
            .get().last().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs) {
            assertNull(output.poster?.url)
            for (data in testData)
                verify(data, never()).media
        }
    }

    // ---

    private fun favorite_responds_success(): Boolean {
        val value = nextBoolean()
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(Result.success(value))
        wheneverBlocking { favorite.toggle(any()) }.thenReturn(Result.success(value))
        return value
    }

    private fun promo_responds_success(): MoviePromoPoster {
        val poster = mock<MoviePromoPoster> {
            on { url }.thenReturn("https://examp.le/image/${nextInt()}")
        }
        wheneverBlocking { promo.get(any()) }.thenReturn(Result.success(poster))
        return poster
    }

    private fun preview_responds_success(
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
        wheneverBlocking { previewFork.get() }.thenReturn(Result.success(content.asSequence()))
        return content
    }

    private fun preview_responds_failure() {
        wheneverBlocking { previewFork.get() }.thenReturn(Result.failure(RuntimeException()))
    }

    private suspend fun ListingFacade.get(): List<Result<ListingFacade.Action>> {
        val outputs = mutableListOf<Result<ListingFacade.Action>>()
        get { outputs += it }
        return outputs
    }

    private suspend fun ListingFacade.Action.promotions(): List<Result<List<MovieView>>> {
        val outputs = mutableListOf<Result<List<MovieView>>>()
        promotions { outputs += it }
        return outputs
    }

    private suspend fun ListingFacade.Action.groupUp(): List<Result<Map<Genre, List<MovieView>>>> {
        val outputs = mutableListOf<Result<Map<Genre, List<MovieView>>>>()
        groupUp { outputs += it }
        return outputs
    }

}