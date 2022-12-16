package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.test.runTest
import movie.core.MovieWithShowings
import movie.core.model.Cinema
import movie.core.model.MovieReference
import movie.core.model.Showing
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.adapter.CinemaFromView
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.Date
import kotlin.random.Random.Default.nextInt
import kotlin.test.assertNotNull

class CinemaFacadeTest : FeatureTest() {

    private lateinit var cinema: Cinema
    private lateinit var facade: CinemaFacade

    override fun prepare() {
        facade = FacadeModule().cinema(event).create("1031")
        cinema = mock()
        whenever(cinema.id).thenReturn("1031")
    }

    @Test
    fun returns_cinema_withIdenticalId() = runTest {
        whenever(event.getCinemas(null)).thenReturn(Result.success(listOf(cinema)))
        val result = facade.getCinema()
        assert(result.isSuccess) { result }
        assertNotNull(result.getOrNull())
    }

    @Test
    fun returns_failure_ifNotFound() = runTest {
        whenever(event.getCinemas(null)).thenReturn(Result.success(emptyList()))
        val result = facade.getCinema()
        assert(result.isFailure)
    }

    @Test
    fun returns_showings_ifCinemaExists() = runTest {
        whenever(event.getCinemas(null))
            .thenReturn(Result.success(listOf(cinema)))
        val view = CinemaFromView(facade.getCinema().getOrThrow())
        val showings = generateShowings(1)
        whenever(event.getShowings(view, Date(0)))
            .thenReturn(Result.success(showings))
        val result = facade.getShowings(Date(0))
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().size == 1)
    }

    @Test
    fun returns_failure_ifCinemaNotFound() = runTest {
        whenever(event.getCinemas(null)).thenReturn(Result.success(emptyList()))
        val result = facade.getShowings(Date(0))
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_sortedShowings() = runTest {
        whenever(event.getCinemas(null))
            .thenReturn(Result.success(listOf(cinema)))
        val view = CinemaFromView(facade.getCinema().getOrThrow())
        val showings = generateShowings(4)
        whenever(event.getShowings(view, Date(0)))
            .thenReturn(Result.success(showings))
        val result = facade.getShowings(Date(0))
        assert(result.isSuccess) { result }
        var previousSize = Int.MAX_VALUE
        for (item in result.getOrThrow())
            assert(previousSize >= item.availability.size) {
                "Showings are not ordered correctly, previous item ($previousSize) has less items than this (${item.availability.size})"
            }.also {
                previousSize = item.availability.size
            }
    }

    @Test
    fun returns_filteredShowings() = runTest {
        whenever(event.getCinemas(null))
            .thenReturn(Result.success(listOf(cinema)))
        val view = CinemaFromView(facade.getCinema().getOrThrow())
        val showings = generateShowings(4)
        whenever(event.getShowings(view, Date(0)))
            .thenReturn(Result.success(showings))
        facade.getShowings(Date(0)) // only to populate the filters
        facade.toggle(Filter(true, "type"))
        facade.toggle(Filter(true, "language"))
        val result = facade.getShowings(Date(0))
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().isEmpty()) { result.getOrThrow() }
    }

    // ---

    private fun generateShowings(count: Int): MovieWithShowings = buildMap {
        repeat(count) {
            val key: MovieReference = mock()
            val items = List(nextInt(1, 20)) {
                mock<Showing>(name = "$it") {
                    on { types }.thenReturn(listOf("type"))
                    on { language }.thenReturn("language")
                }
            }
            put(key, items)
        }
    }

}