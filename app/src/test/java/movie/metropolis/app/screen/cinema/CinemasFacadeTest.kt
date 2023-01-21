package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.test.runTest
import movie.core.model.Cinema
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.util.callback
import movie.metropolis.app.util.thenBlocking
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertFails

class CinemasFacadeTest : FeatureTest() {

    private lateinit var facade: CinemasFacade

    override fun prepare() {
        facade = FacadeModule().cinemas(cinema)
    }

    @Test
    fun returns_listOfAll_withoutLocation() = runTest {
        cinema_responds_success(13)
        facade.getCinemas(null, null) {
            val result = it.getOrThrow()
            assert(result.size == 13) { "Expected to contain 13 elements. $result" }
        }
    }

    @Test
    fun returns_listOfLimited_withLocation() = runTest {
        cinema_responds_success(6)
        facade.getCinemas(1.0, 1.0) {
            val result = it.getOrThrow()
            assert(result.size == 6) { "Expected to contain 6 elements. $result" }
        }
    }

    @Test
    fun returns_failure() = runTest {
        facade.getCinemas(1.0, 1.0) {
            assertFails {
                it.getOrThrow()
            }
        }
    }

    // ---

    private suspend fun cinema_responds_success(count: Int) {
        whenever(cinema.get(anyOrNull(), any())).thenBlocking {
            callback<Iterable<Cinema>>(1) {
                Result.success(List(count) { mock() })
            }
        }
    }

}