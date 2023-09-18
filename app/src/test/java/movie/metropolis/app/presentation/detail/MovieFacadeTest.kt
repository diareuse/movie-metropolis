@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.detail

import androidx.compose.ui.graphics.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import movie.core.CinemaWithShowings
import movie.core.adapter.MovieFromId
import movie.core.model.Cinema
import movie.core.model.Location
import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.core.model.Showing
import movie.image.Swatch
import movie.image.SwatchColor
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Filter
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.disableAll
import movie.rating.MovieMetadata
import org.junit.Test
import org.mockito.kotlin.KStubbing
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals
import kotlin.test.assertFails

class MovieFacadeTest : FeatureTest() {

    private lateinit var facade: MovieFacade.Filterable

    override fun prepare() {
        facade =
            FacadeModule().movie(showings, detail, favorite, analyzer, rating).create("5376O2R")
    }

    @Test
    fun returns_isFavorite_success() = runTest {
        movie_responds_success()
        whenever(favorite.isFavorite(any())).thenReturn(Result.success(true))
        facade.favorite.first()
    }

    @Test
    fun returns_isFavorite_failure() = runTest {
        movie_responds_success()
        whenever(favorite.isFavorite(any())).thenThrow(RuntimeException())
        assertEquals(false, facade.favorite.first())
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
        assertEquals(Date(3666821600000), facade.availability.first())
    }

    @Test
    fun returns_availableFrom_recoversFailure() = runTest {
        facade.availability.first()
    }

    @Test
    fun returns_movie_success() = runTest {
        movie_responds_success {
            on { releasedAt }.thenReturn(Date())
            on { originalName }.thenReturn("")
            on { name }.thenReturn("")
        }
        facade.movie.first()
    }

    @Test
    fun returns_movie_failure() = runTest {
        assertFails {
            facade.movie.first().getOrThrow()
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
        facade.showings(Date(0), 0.0, 0.0).first().getOrThrow()
    }

    @Test
    fun returns_showings_failure() = runTest {
        assertFails {
            facade.showings(Date(0), 0.0, 0.0).first().getOrThrow()
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
        facade.showings(Date(0), 0.0, 0.0).first() // only to populate the filters
        facade.disableAll()
        facade.toggle(Filter(false, "type"))
        facade.toggle(Filter(false, "language"))
        val result = facade.showings(Date(0), 0.0, 0.0).first().getOrThrow()
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
        facade.showings(Date(0), 0.0, 0.0).first()
        val options = facade.options.first()
        assertEquals(2, options[Filter.Type.Language]?.size)
        assertEquals(2, options[Filter.Type.Projection]?.size)
    }

    @Test
    fun toggle_notifiesListeners() = runTest {
        val outputs = mutableListOf<Result<*>>()
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            facade.showings(Date(), 0.0, 0.0).toList(outputs)
        }
        facade.toggle(Filter(false, ""))
        assertEquals(2, outputs.size)
    }

    @Test
    fun detail_returns_color() = runTest {
        movie_responds_success {
            on { releasedAt }.thenReturn(Date())
            on { originalName }.thenReturn("")
            on { name }.thenReturn("")
            on { media }.thenReturn(listOf(Media.Image(0, 0, "")))
        }
        val color = analyzer_responds_success()
        val movie = facade.movie.last().getOrThrow()
        assertEquals(color, movie.poster?.spotColor)
    }

    @Test
    fun detail_returns_rating() = runTest {
        movie_responds_success {
            on { releasedAt }.thenReturn(Date())
            on { originalName }.thenReturn("")
            on { name }.thenReturn("")
        }
        val rating = rating_responds_success()
        val movie = facade.movie.last().getOrThrow()
        assertEquals("%d%%".format(rating.rating), movie.rating)
    }

    // ---

    private suspend fun rating_responds_success(): MovieMetadata {
        val composed = MovieMetadata(69, "", "")
        whenever(rating.get(any())).thenReturn(composed)
        return composed
    }

    private suspend fun analyzer_responds_success(): Color {
        val color = nextInt(0, 0xffffff)
        val swatch = SwatchColor(color)
        whenever(analyzer.getColors(any())).thenReturn(Swatch(swatch, swatch, swatch))
        return Color(color)
    }

    private suspend fun movie_responds_success(
        modifier: KStubbing<MovieDetail>.(MovieDetail) -> Unit = {}
    ) {
        val data = mock(stubbing = modifier)
        whenever(detail.get(any())).thenReturn(Result.success(data))
    }

    private suspend fun cinema_responds_cinema(modifier: KStubbing<Cinema>.(Cinema) -> Unit = {}): Cinema {
        val model = mock(stubbing = modifier)
        whenever(cinema.get(anyOrNull())).thenReturn(Result.success(sequenceOf(model)))
        return model
    }

    private suspend fun showings_responds_success(model: CinemaWithShowings = mock()) {
        val movie = showings.movie(MovieFromId(""), Location(0.0, 0.0))
        whenever(movie.get(any())).thenReturn(Result.success(model))
    }

    private fun generateShowings(cinema: Cinema, count: Int): CinemaWithShowings = buildMap {
        repeat(count) {
            val items = List<Showing>(nextInt(2, 20)) {
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