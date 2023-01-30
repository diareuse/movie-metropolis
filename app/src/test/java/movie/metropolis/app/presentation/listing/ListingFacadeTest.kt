@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.listing

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.EventPreviewFeature
import movie.core.model.MoviePreview
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.adapter.MovieViewFromFeature
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.util.callback
import movie.metropolis.app.util.thenBlocking
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.internal.verification.NoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class ListingFacadeTest : FeatureTest() {

    private lateinit var facade: ListingFacade

    override fun prepare() {
        wheneverBlocking { favorite.isFavorite(any()) }.thenReturn(Result.success(true))
        facade = FacadeModule().listing(preview, favorite)
    }

    @Test
    fun returns_current_success() = runTest {
        responds_success(preview.current(), 34)
        facade.getCurrent {
            assertEquals(34, it.getOrThrow().size)
        }
    }

    @Test
    fun returns_current_failure() = runTest {
        responds_failure(preview.current())
        facade.getCurrent {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_upcoming_success() = runTest {
        responds_success(preview.upcoming(), 18)
        facade.getUpcoming {
            assertEquals(18, it.getOrThrow().size)
        }
    }

    @Test
    fun returns_upcoming_failure() = runTest {
        responds_failure(preview.upcoming())
        facade.getUpcoming {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Suppress("BooleanLiteralArgument")
    @Test
    fun toggleFavorite_callsFavorite() = runTest {
        facade.toggleFavorite(MovieViewFromFeature(mock(), true))
        verify(favorite).toggle(any())
    }

    @Test
    fun returns_current_withFavorites() = runTest {
        responds_success(preview.current(), 5)
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        var result: Result<List<MovieView>> = Result.failure(NotImplementedError())
        facade.getCurrent { result = it }
        assertTrue {
            result.getOrThrow().all { it.favorite }
        }
    }

    @Test
    fun returns_upcoming_withFavorites() = runTest {
        responds_success(preview.upcoming(), 5)
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        var result: Result<List<MovieView>> = Result.failure(NotImplementedError())
        facade.getUpcoming { result = it }
        assertTrue {
            result.getOrThrow().all { it.favorite }
        }
    }

    @Test
    fun returns_current_withoutFavorites() = runTest {
        responds_success(preview.current(), 5)
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(false))
        var result: Result<List<MovieView>> = Result.failure(NotImplementedError())
        facade.getCurrent { result = it }
        assertTrue {
            result.getOrThrow().none { it.favorite }
        }
    }

    @Test
    fun returns_upcoming_withoutFavorites() = runTest {
        responds_success(preview.upcoming(), 5)
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(false))
        var result: Result<List<MovieView>> = Result.failure(NotImplementedError())
        facade.getUpcoming { result = it }
        assertTrue {
            result.getOrThrow().none { it.favorite }
        }
    }

    @Test
    fun listener_notifiesOnToggle() = runTest {
        val listener = mock<OnChangedListener>()
        facade.addOnFavoriteChangedListener(listener)
        facade.toggleFavorite(MovieViewFromFeature(mock(), true))
        verify(listener).onChanged()
    }

    @Test
    fun listener_doesNot_notifyRemovedListeners() = runTest {
        val listener = mock<OnChangedListener>()
        facade.addOnFavoriteChangedListener(listener)
        facade.removeOnFavoriteChangedListener(listener)
        facade.toggleFavorite(MovieViewFromFeature(mock(), true))
        verify(listener, NoInteractions()).onChanged()
    }

    // ---

    private suspend fun responds_success(feature: EventPreviewFeature, count: Int) {
        whenever(feature.get(any())).thenBlocking {
            callback<List<MoviePreview>>(0) {
                Result.success(List(count) { mock() })
            }
        }
    }

    private suspend fun responds_failure(feature: EventPreviewFeature) {
        whenever(feature.get(any())).thenBlocking {
            callback<List<MoviePreview>>(0) {
                Result.failure(RuntimeException())
            }
        }
    }

}