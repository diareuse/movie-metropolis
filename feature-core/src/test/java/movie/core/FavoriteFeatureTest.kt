package movie.core

import kotlinx.coroutines.test.runTest
import movie.core.db.dao.MovieDao
import movie.core.db.dao.MovieFavoriteDao
import movie.core.di.FavoriteFeatureModule
import movie.core.mock.MovieFavoriteStored
import movie.core.mock.MovieStored
import movie.core.model.Movie
import movie.core.model.MoviePreview
import movie.pulse.ExactPulseScheduler
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FavoriteFeatureTest {

    private lateinit var scheduler: ExactPulseScheduler
    private lateinit var feature: FavoriteFeature
    private lateinit var movieDao: MovieDao
    private lateinit var favorite: MovieFavoriteDao

    @Before
    fun prepare() {
        favorite = mock()
        movieDao = mock()
        scheduler = mock {
            on { schedule(any()) }.then { }
            on { cancel(any()) }.then { }
        }
        feature = FavoriteFeatureModule().feature(favorite, movieDao, scheduler)
    }

    @Test
    fun isFavorite_returnsTrue() = runTest {
        val movie = mock<Movie> {
            on { id }.thenReturn("id")
        }
        whenever(favorite.isFavorite(movie.id)).thenReturn(true)
        assertTrue(feature.isFavorite(movie))
    }

    @Test
    fun isFavorite_returnsFalse() = runTest {
        val movie = mock<Movie> {
            on { id }.thenReturn("id")
        }
        whenever(favorite.isFavorite(movie.id)).thenReturn(false)
        assertFalse(feature.isFavorite(movie))
    }

    @Test
    fun isFavorite_returnsFailure() = runTest {
        val movie = mock<Movie> {
            on { id }.thenReturn("id")
        }
        whenever(favorite.isFavorite(movie.id)).thenThrow(RuntimeException())
        assertFails {
            feature.isFavorite(movie)
        }
    }

    @Test
    fun toggle_inserts() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(0))
            on { releasedAt }.thenReturn(Date(0))
        }
        whenever(favorite.select(movie.id)).thenReturn(MovieFavoriteStored())
        whenever(favorite.isFavorite(movie.id)).thenReturn(false)
        whenever(movieDao.select(movie.id)).thenReturn(MovieStored())
        feature.toggle(movie)
        verify(favorite).insertOrUpdate(any())
    }

    @Test
    fun toggle_deletes() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(0))
            on { releasedAt }.thenReturn(Date(0))
        }
        whenever(favorite.isFavorite(movie.id)).thenReturn(true)
        feature.toggle(movie)
        verify(favorite).delete(any())
    }

    @Test
    fun toggle_returnsFailure_onInsert() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(0))
        }
        whenever(favorite.isFavorite(any())).thenReturn(false)
        whenever(favorite.insertOrUpdate(any())).thenThrow(RuntimeException())
        assertFails {
            feature.toggle(movie)
        }
    }

    @Test
    fun toggle_returnsFailure_onDelete() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(0))
        }
        whenever(favorite.isFavorite(any())).thenReturn(true)
        whenever(favorite.delete(any())).thenThrow(RuntimeException())
        assertFails {
            feature.toggle(movie)
        }
    }

    @Test
    fun toggle_returnsFailure_isFavoriteError() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(0))
        }
        whenever(favorite.isFavorite(any())).thenThrow(RuntimeException())
        assertFails {
            feature.toggle(movie)
        }
    }

    @Test
    fun toggle_cancelsAlarm() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(0))
            on { releasedAt }.thenReturn(Date(0))
        }
        whenever(favorite.isFavorite(any())).thenReturn(true)
        feature.toggle(movie)
        verify(scheduler).cancel(any())
    }

    @Test
    fun toggle_schedulesAlarm() = runTest {
        val movie = mock<MoviePreview> {
            on { id }.thenReturn("id")
            on { screeningFrom }.thenReturn(Date(System.currentTimeMillis() + 10000))
            on { releasedAt }.thenReturn(Date(System.currentTimeMillis() + 10000))
        }
        whenever(favorite.isFavorite(any())).thenReturn(false)
        whenever(favorite.select(movie.id)).thenReturn(MovieFavoriteStored(false))
        whenever(movieDao.select(movie.id)).thenReturn(MovieStored())
        feature.toggle(movie)
        verify(scheduler).schedule(any())
    }

    @Test
    fun getAll_returnsValue() = runTest {
        val previews = List(1) { MovieFavoriteStored() }
        whenever(favorite.selectAll()).thenReturn(previews)
        whenever(movieDao.select(any())).thenReturn(MovieStored())
        assertTrue("Values should not be empty") { feature.getAll().isNotEmpty() }
    }

    @Test
    fun getAll_returnsFailure() = runTest {
        whenever(favorite.selectAll()).thenThrow(RuntimeException())
        assertFails {
            feature.getAll()
        }
    }

}