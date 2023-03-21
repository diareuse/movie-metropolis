@file:OptIn(ExperimentalCoroutinesApi::class)

package movie.metropolis.app.presentation.cinema

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import movie.core.MovieWithShowings
import movie.core.MutableResult
import movie.core.model.Cinema
import movie.core.model.MovieReference
import movie.core.model.Showing
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Filter
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.util.disableAll
import org.junit.Test
import org.mockito.internal.verification.NoInteractions
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertEquals
import kotlin.test.assertFails

class CinemaFacadeTest : FeatureTest() {

    private lateinit var model: Cinema
    private lateinit var facade: CinemaFacade

    override fun prepare() {
        facade = FacadeModule().cinema(cinema, showings).create("1031")
        model = mock()
        whenever(model.id).thenReturn("1031")
    }

    @Test
    fun returns_cinema_withIdenticalId() = runTest {
        cinemas_returns_cinema()
        facade.getCinema {
            val result = it.getOrThrow()
            val expected = model.id
            assertEquals(expected, result.id)
        }
    }

    @Test
    fun returns_failure_ifNotFound() = runTest {
        cinemas_returns_empty()
        facade.getCinema {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_showings_ifCinemaExists() = runTest {
        cinemas_returns_cinema()
        showings_returns_value(1)
        val result = MutableResult.getOrThrow {
            facade.showings(Date(0), it.asResultCallback())
        }
        assertEquals(1, result.size)
    }

    @Test
    fun returns_failure_ifCinemaNotFound() = runTest {
        cinemas_returns_empty()
        facade.showings(Date(0)) {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    @Test
    fun returns_sortedShowings() = runTest {
        cinemas_returns_cinema()
        showings_returns_value(4)
        facade.showings(Date(0)) {
            var previousSize = Int.MAX_VALUE
            for (item in it.getOrThrow())
                assert(previousSize >= item.availability.size) {
                    "Showings are not ordered correctly, previous item ($previousSize) has less items than this (${item.availability.size})"
                }.also {
                    previousSize = item.availability.size
                }
        }
    }

    @Test
    fun returns_filteredShowings() = runTest {
        cinemas_returns_cinema()
        showings_returns_value(4)
        facade.showings(Date(0)) {} // only to populate the filters
        facade.disableAll()
        facade.toggle(Filter(false, "type"))
        facade.toggle(Filter(false, "language"))
        facade.showings(Date(0)) {
            val result = it.getOrThrow()
            val hasRequestedKeys = result.flatMap { it.availability.keys }
                .all { it.language == "language" && "type" in it.types }
            assert(hasRequestedKeys) { result }
        }
    }

    @Test
    fun returns_options() = runTest {
        cinemas_returns_cinema()
        showings_returns_value(4)
        facade.showings(Date(0)) {}
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
    fun toggle_notifiesListeners_onUpdate() = runTest {
        cinemas_returns_cinema()
        showings_returns_value(4)
        val listener = mock<OnChangedListener>()
        facade.addOnChangedListener(listener)
        facade.showings(Date()) { it.getOrThrow() }
        facade.showings(Date()) { it.getOrThrow() }
        // intentionally invoking twice, listener should be invoked just once
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

    private suspend fun cinemas_returns_cinema() {
        whenever(cinema.get(anyOrNull())).thenReturn(Result.success(sequenceOf(model)))
    }

    private suspend fun cinemas_returns_empty() {
        whenever(cinema.get(anyOrNull())).thenReturn(Result.success(emptySequence()))
    }

    private suspend fun showings_returns_value(count: Int = 1) {
        val data = generateShowings(count)
        whenever(showings.cinema(model).get(any())).thenReturn(Result.success(data))
    }

    private fun generateShowings(count: Int): MovieWithShowings = buildMap {
        repeat(count) {
            val key: MovieReference = mock()
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
            put(key, items)
        }
    }

}