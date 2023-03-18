@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.detail

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.CinemaWithShowings
import movie.core.MutableResult
import movie.core.adapter.MovieFromId
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.core.model.Showing
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Filter
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.util.callback
import movie.metropolis.app.util.disableAll
import movie.metropolis.app.util.thenBlocking
import org.junit.Test
import org.mockito.internal.verification.NoInteractions
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFails

class MovieFacadeTest : FeatureTest() {

    private lateinit var facade: MovieFacade

    override fun prepare() {
        facade = FacadeModule().movie(showings, detail, favorite).create("5376O2R")
    }

    @Test
    fun returns_isFavorite_success() = runTest {
        movie_responds_success()
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        facade.isFavorite().getOrThrow()
    }

    @Test
    fun returns_isFavorite_failure() = runTest {
        movie_responds_success()
        whenever(favorite.isFavorite(any())).thenThrow(RuntimeException())
        assertFails {
            facade.isFavorite().getOrThrow()
        }
    }

    @Test
    fun toggle_callsFavorite() = runTest {
        movie_responds_success()
        facade.toggleFavorite()
        verify(favorite).toggle(any())
    }

    @Test
    fun returns_availableFrom_success() = runTest {
        movie_responds_success {
            on { it.screeningFrom }.thenReturn(Date(3666821600000))
        }
        facade.getAvailableFrom {
            assertEquals(Date(3666821600000), it.getOrThrow())
        }
    }

    @Test
    fun returns_availableFrom_failure() = runTest {
        facade.getAvailableFrom {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_movie_success() = runTest {
        movie_responds_success()
        facade.getMovie {
            it.getOrThrow()
        }
    }

    @Test
    fun returns_movie_failure() = runTest {
        facade.getMovie {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_poster_success() = runTest {
        movie_responds_success {
            on { it.media }.thenReturn(listOf(Media.Image(0, 0, "")))
        }
        facade.getPoster {
            it.getOrThrow()
        }
    }

    @Test
    fun returns_poster_failure() = runTest {
        facade.getPoster {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_trailer_success() = runTest {
        movie_responds_success {
            on { it.media }.thenReturn(listOf(Media.Video("")))
        }
        facade.getTrailer {
            it.getOrThrow()
        }
    }

    @Test
    fun returns_trailer_failure() = runTest {
        facade.getTrailer {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_showings_success() = runTest {
        movie_responds_success {
            on { id }.thenReturn("5376O2R")
        }
        cinema_responds_cinema {
            on { id }.thenReturn("id")
        }
        showings_responds_success()
        facade.getShowings(Date(0), 0.0, 0.0) {
            it.getOrThrow()
        }
    }

    @Test
    fun returns_showings_failure() = runTest {
        facade.getShowings(Date(0), 0.0, 0.0) {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_filteredShowings() = runTest {
        movie_responds_success {
            on { id }.thenReturn("5376O2R")
        }
        val cinema = cinema_responds_cinema {
            on { id }.thenReturn("id")
        }
        showings_responds_success(generateShowings(cinema, 4))
        facade.getShowings(Date(0), 0.0, 0.0) {} // only to populate the filters
        facade.disableAll()
        facade.toggle(Filter(false, "type"))
        facade.toggle(Filter(false, "language"))
        val result = MutableResult.getOrThrow {
            facade.getShowings(Date(0), 0.0, 0.0, it.asResultCallback())
        }
        val hasRequestedKeys = result.flatMap { it.availability.keys }
            .all { it.language == "language" && "type" in it.types }
        assert(hasRequestedKeys) { result }
    }

    @Test
    fun returns_options() = runTest {
        movie_responds_success()
        val cinema = cinema_responds_cinema {
            on { id }.thenReturn("id")
        }
        showings_responds_success(generateShowings(cinema, 4))
        facade.getShowings(Date(0), 0.0, 0.0) {}
        val options = facade.getOptions().getOrThrow()
        assertEquals(2, options[Filter.Type.Language]?.size)
        assertEquals(2, options[Filter.Type.Projection]?.size)
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

    private suspend fun movie_responds_success(
        modifier: KStubbing<MovieDetail>.(MovieDetail) -> Unit = {}
    ) {
        whenever(detail.get(any(), any())).thenBlocking {
            callback(1) {
                Result.success(mock(stubbing = modifier))
            }
        }
    }

    private suspend fun cinema_responds_cinema(modifier: KStubbing<Cinema>.(Cinema) -> Unit = {}): Cinema {
        val model = mock(stubbing = modifier)
        whenever(cinema.get(anyOrNull())).thenReturn(Result.success(sequenceOf(model)))
        return model
    }

    private suspend fun showings_responds_success(model: CinemaWithShowings = mock()) {
        val movie = showings.movie(MovieFromId(""), Location(0.0, 0.0))
        whenever(movie.get(any(), any())).thenBlocking {
            callback(1) {
                Result.success(model)
            }
        }
    }

    private fun generateShowings(cinema: Cinema, count: Int): CinemaWithShowings = buildMap {
        repeat(count) {
            val items = List<Showing>(Random.nextInt(2, 20)) {
                when (it % 2) {
                    1 -> mock(name = "$it") {
                        on { types }.thenReturn(listOf("type", "type2"))
                        on { language }.thenReturn("language")
                    }

                    else -> mock(name = "$it") {
                        on { types }.thenReturn(listOf("type2"))
                        on { language }.thenReturn("language2")
                    }
                }
            }
            put(cinema, items)
        }
    }

}