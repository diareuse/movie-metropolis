package movie.metropolis.app.presentation.favorite

import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import movie.core.model.Movie
import movie.core.model.MovieDetail
import movie.core.model.MovieFavorite
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.FeatureTest
import movie.rating.MovieMetadata
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class FavoriteFacadeTest : FeatureTest() {

    private lateinit var facade: FavoriteFacade

    override fun prepare() {
        facade = FacadeModule().favorite(favorite, detail, rating)
    }

    @Test
    fun returns_rating() = runTest {
        favorite_responds_success()
        detail_responds_success()
        val rating = rating_responds_success()
        val values = mutableListOf<List<MovieView>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            facade.get().toList(values)
        }
        advanceUntilIdle()
        for (item in values.last())
            assertEquals("%d%%".format(rating.rating), item.rating)
    }

    @Test
    fun returns_data() = runTest {
        favorite_responds_success()
        detail_responds_success()
        val values = mutableListOf<List<MovieView>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            facade.get().toList(values)
        }
        advanceUntilIdle()
        assert(values.last().isNotEmpty())
    }

    @Test
    fun remove_triggers_update() = runTest {
        favorite_responds_success()
        detail_responds_success()
        val values = mutableListOf<List<MovieView>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            facade.get().toList(values)
        }
        advanceUntilIdle()
        val initialSize = values.size
        val mock = mock<Movie>()
        facade.remove(mock {
            on { getBase() }.thenReturn(mock)
        })
        advanceUntilIdle()
        assertNotEquals(initialSize, values.size)
    }

    // ---

    private suspend fun favorite_responds_success(): List<MovieFavorite> {
        val mockMovie = mock<Movie> {
            on { id }.thenReturn("id")
        }
        val favorites = List(1) {
            mock<MovieFavorite> {
                on { movie }.thenReturn(mockMovie)
            }
        }
        whenever(favorite.getAll()).thenReturn(Result.success(favorites))
        return favorites
    }

    private suspend fun detail_responds_success(): MovieDetail {
        val item = mock<MovieDetail> {
            on { releasedAt }.thenReturn(Date())
            on { name }.thenReturn("name")
            on { originalName }.thenReturn("original")
        }
        whenever(detail.get(any())).thenReturn(item)
        return item
    }

    private suspend fun rating_responds_success(): MovieMetadata {
        val metadata = mock<MovieMetadata> {
            on { rating }.thenReturn(12)
        }
        whenever(rating.get(any())).thenReturn(metadata)
        return metadata
    }

}