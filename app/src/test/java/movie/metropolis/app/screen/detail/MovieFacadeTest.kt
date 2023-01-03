package movie.metropolis.app.screen.detail

import kotlinx.coroutines.test.runTest
import movie.core.CinemaWithShowings
import movie.core.FavoriteFeature
import movie.core.adapter.MovieFromId
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.core.model.Showing
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Filter
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.OnChangedListener
import org.junit.Test
import org.mockito.internal.verification.NoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFails

class MovieFacadeTest : FeatureTest() {

    private lateinit var favorite: FavoriteFeature
    private lateinit var facade: MovieFacade

    override fun prepare() {
        favorite = mock()
        facade = FacadeModule().movie(event, favorite).create("5376O2R")
    }

    @Test
    fun returns_isFavorite_success() = runTest {
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(mock()))
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        facade.isFavorite().getOrThrow()
    }

    @Test
    fun returns_isFavorite_failure() = runTest {
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(mock()))
        whenever(favorite.isFavorite(any())).thenThrow(RuntimeException())
        assertFails {
            facade.isFavorite().getOrThrow()
        }
    }

    @Test
    fun toggle_callsFavorite() = runTest {
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(mock()))
        facade.toggleFavorite()
        verify(favorite).toggle(any())
    }

    @Test
    fun returns_availableFrom_success() = runTest {
        val detail = mock<MovieDetail> {
            on { it.screeningFrom }.thenReturn(Date(3666821600000))
        }
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(detail))
        val result = facade.getAvailableFrom()
        assert(result.isSuccess) { result }
        assertEquals(Date(3666821600000), result.getOrThrow())
    }

    @Test
    fun returns_availableFrom_failure() = runTest {
        val result = facade.getAvailableFrom()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_movie_success() = runTest {
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(mock()))
        val result = facade.getMovie()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_movie_failure() = runTest {
        val result = facade.getMovie()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_poster_success() = runTest {
        val detail = mock<MovieDetail> {
            on { it.media }.thenReturn(listOf(Media.Image(0, 0, "")))
        }
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(detail))
        val result = facade.getPoster()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_poster_failure() = runTest {
        val result = facade.getPoster()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_trailer_success() = runTest {
        val detail = mock<MovieDetail> {
            on { it.media }.thenReturn(listOf(Media.Video("")))
        }
        whenever(event.getDetail(MovieFromId("5376O2R"))).thenReturn(Result.success(detail))
        val result = facade.getTrailer()
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_trailer_failure() = runTest {
        val result = facade.getTrailer()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_showings_success() = runTest {
        val movie = mock<MovieDetail> {
            on { id }.thenReturn("5376O2R")
        }
        whenever(event.getDetail(MovieFromId("5376O2R")))
            .thenReturn(Result.success(movie))
        val cinema = mock<Cinema> {
            on { id }.thenReturn("id")
        }
        whenever(event.getCinemas(Location(0.0, 0.0)))
            .thenReturn(Result.success(listOf(cinema)))
        whenever(event.getShowings(movie, Date(0), Location(0.0, 0.0)))
            .thenReturn(Result.success(mock()))
        val result = facade.getShowings(Date(0), 0.0, 0.0)
        assert(result.isSuccess) { result }
    }

    @Test
    fun returns_showings_failure() = runTest {
        val result = facade.getShowings(Date(0), 0.0, 0.0)
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_filteredShowings() = runTest {
        val movie = mock<MovieDetail> {
            on { id }.thenReturn("5376O2R")
        }
        whenever(event.getDetail(MovieFromId("5376O2R")))
            .thenReturn(Result.success(movie))
        val cinema = mock<Cinema> {
            on { id }.thenReturn("id")
        }
        whenever(event.getCinemas(Location(0.0, 0.0)))
            .thenReturn(Result.success(listOf(cinema)))
        val showings = generateShowings(cinema, 4)
        whenever(event.getShowings(movie, Date(0), Location(0.0, 0.0)))
            .thenReturn(Result.success(showings))
        facade.getShowings(Date(0), 0.0, 0.0) // only to populate the filters
        facade.toggle(Filter(true, "type"))
        facade.toggle(Filter(true, "language"))
        val result = facade.getShowings(Date(0), 0.0, 0.0)
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().isEmpty()) { result.getOrThrow() }
    }

    @Test
    fun returns_options() = runTest {
        val movie = mock<MovieDetail> {
            on { id }.thenReturn("5376O2R")
        }
        whenever(event.getDetail(MovieFromId("5376O2R")))
            .thenReturn(Result.success(movie))
        val cinema = mock<Cinema> {
            on { id }.thenReturn("id")
        }
        whenever(event.getCinemas(Location(0.0, 0.0)))
            .thenReturn(Result.success(listOf(cinema)))
        val showings = generateShowings(cinema, 4)
        whenever(event.getShowings(movie, Date(0), Location(0.0, 0.0)))
            .thenReturn(Result.success(showings))
        facade.getShowings(Date(0), 0.0, 0.0)
        val options = facade.getOptions().getOrThrow()
        assertEquals(1, options[Filter.Type.Language]?.size)
        assertEquals(1, options[Filter.Type.Projection]?.size)
    }

    @Test
    fun toggle_notifiesListeners() = runTest {
        val listener = mock<OnChangedListener>()
        facade.addOnChangedListener(listener)
        facade.toggle(Filter(false, ""))
        verify(listener).onChanged()
    }

    @Test
    fun toggle_notifiesNoRemovedListeners() = runTest {
        val listener = mock<OnChangedListener>()
        facade.addOnChangedListener(listener)
        facade.removeOnChangedListener(listener)
        facade.toggle(Filter(false, ""))
        verify(listener, NoInteractions()).onChanged()
    }

    // ---

    private fun generateShowings(cinema: Cinema, count: Int): CinemaWithShowings = buildMap {
        repeat(count) {
            val items = List(Random.nextInt(1, 20)) {
                mock<Showing>(name = "$it") {
                    on { types }.thenReturn(listOf("type"))
                    on { language }.thenReturn("language")
                }
            }
            put(cinema, items)
        }
    }

}