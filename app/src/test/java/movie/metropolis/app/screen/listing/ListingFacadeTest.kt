package movie.metropolis.app.screen.listing

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import movie.core.FavoriteFeature
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.adapter.MovieViewFromFeature
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ListingFacadeTest : FeatureTest() {

    private lateinit var favorite: FavoriteFeature
    private lateinit var facade: ListingFacade

    override fun prepare() {
        favorite = mock {
            on { runBlocking { isFavorite(any()) } }.thenReturn(Result.success(true))
        }
        facade = FacadeModule().listing(event, favorite)
    }

    @Test
    fun returns_current_success() = runTest {
        whenever(event.getCurrent()).thenReturn(Result.success(List(34) { mock() }))
        val result = facade.getCurrent()
        assert(result.isSuccess) { result }
        assertEquals(34, result.getOrThrow().size)
    }

    @Test
    fun returns_current_failure() = runTest {
        whenever(event.getCurrent()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getCurrent()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_upcoming_success() = runTest {
        whenever(event.getUpcoming()).thenReturn(Result.success(List(18) { mock() }))
        val result = facade.getUpcoming().getOrThrow()
        assertEquals(18, result.size)
    }

    @Test
    fun returns_upcoming_failure() = runTest {
        whenever(event.getUpcoming()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getUpcoming()
        assert(result.isFailure) { result }
    }

    @Suppress("BooleanLiteralArgument")
    @Test
    fun toggleFavorite_callsFavorite() = runTest {
        facade.toggleFavorite(MovieViewFromFeature(mock(), true, true))
        verify(favorite).toggle(any())
    }

    @Test
    fun returns_current_withFavorites() = runTest {
        whenever(event.getCurrent()).thenReturn(Result.success(List(5) { mock() }))
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        val result = facade.getCurrent().getOrThrow()
        assertTrue {
            result.all { it.favorite }
        }
    }

    @Test
    fun returns_upcoming_withFavorites() = runTest {
        whenever(event.getUpcoming()).thenReturn(Result.success(List(5) { mock() }))
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        val result = facade.getUpcoming().getOrThrow()
        assertTrue {
            result.all { it.favorite }
        }
    }

    @Test
    fun returns_current_withoutFavorites() = runTest {
        whenever(event.getCurrent()).thenReturn(Result.success(List(5) { mock() }))
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(false))
        val result = facade.getCurrent().getOrThrow()
        assertTrue {
            result.none { it.favorite }
        }
    }

    @Test
    fun returns_upcoming_withoutFavorites() = runTest {
        whenever(event.getUpcoming()).thenReturn(Result.success(List(5) { mock() }))
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(false))
        val result = facade.getUpcoming().getOrThrow()
        assertTrue {
            result.none { it.favorite }
        }
    }

}