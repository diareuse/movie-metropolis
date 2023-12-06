package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.test.runTest
import movie.core.EventPreviewFeature
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import movie.core.model.MoviePromoPoster
import movie.image.Swatch
import movie.image.SwatchColor.Companion.White
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.FeatureTest
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
import kotlin.test.assertFalse
import kotlin.test.assertTrue

abstract class ListingFacadeTest : FeatureTest() {

    private lateinit var previewFork: EventPreviewFeature
    lateinit var facade: ListingFacade

    abstract fun choose(factory: ListingFacade.Factory): ListingFacade
    abstract fun choose(factory: EventPreviewFeature.Factory): EventPreviewFeature

    override fun prepare() {
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(Result.success(false))
        facade = FacadeModule()
            .listing(preview, favorite, promo, analyzer, rating, detail)
            .run(::choose)
        previewFork = choose(preview)
    }

    class Upcoming : ListingFacadeTest() {

        override fun choose(factory: ListingFacade.Factory) = factory.upcoming()
        override fun choose(factory: EventPreviewFeature.Factory) = factory.upcoming()

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
        fun promotions_returns_favorite() = runTest {
            preview_responds_success()
            promo_responds_success()
            val value = favorite_responds_success()
            val outputs = facade
                .get().getOrThrow()
                .promotions().last().getOrThrow()
            for (output in outputs)
                assertEquals(value, output.favorite)
        }

    }

    class Current : ListingFacadeTest() {
        override fun choose(factory: ListingFacade.Factory) = factory.current()
        override fun choose(factory: EventPreviewFeature.Factory) = factory.current()
    }

    @Test
    fun promotions_returns_setOfAtMost3() = runTest {
        val count = nextInt(0, 100)
        preview_responds_success(count)
        promo_responds_success()
        val action = facade.get()
        for (output in action.getOrThrow().promotions())
            assertTrue {
                output.getOrThrow().size in 0..3
            }
    }

    @Test
    fun groupUp_returns_moviesInGenres() = runTest {
        val count = nextInt(0, 100)
        detail_responds_success()
        preview_responds_success(count) {
            on { genres }.thenReturn(listOf("1", "2"))
        }
        val action = facade.get()
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
            .get().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs)
            assertEquals(url, output.poster?.url)
    }

    @Test
    fun promotions_returns_withPosterRatio() = runTest {
        preview_responds_success()
        promo_responds_success()
        val outputs = facade
            .get().getOrThrow()
            .promotions().last().getOrThrow()
        for (output in outputs)
            assertEquals(1.5f, output.poster?.aspectRatio)
    }

    @Test
    fun get_fails() = runTest {
        preview_responds_failure()
        val output = facade.get()
        assertFails {
            output.getOrThrow()
        }
    }

    @Test
    fun groups_returns_defaultGroup() = runTest {
        detail_responds_success()
        preview_responds_success {
            on { genres }.thenReturn(emptyList())
        }
        val action = facade.get()
        for (output in action.getOrThrow().groupUp())
            assertContentEquals(
                setOf(Genre("other")),
                output.getOrThrow().keys.asIterable()
            )
    }

    // ---

    private fun analyzer_responds_success(): Int {
        wheneverBlocking { analyzer.getColors(any()) }.thenReturn(Swatch(White, White, White))
        return White.rgb
    }

    private fun detail_responds_success(
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
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(Result.success(value))
        wheneverBlocking { favorite.toggle(any()) }.thenReturn(Result.success(value))
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

    suspend fun ListingFacade.Action.promotions(): List<Result<List<MovieView>>> {
        val outputs = mutableListOf<Result<List<MovieView>>>()
        promotions.collect { outputs += it }
        return outputs
    }

    private suspend fun ListingFacade.Action.groupUp(): List<Result<Map<Genre, List<MovieView>>>> {
        val outputs = mutableListOf<Result<Map<Genre, List<MovieView>>>>()
        groups.collect { outputs += it }
        return outputs
    }

}