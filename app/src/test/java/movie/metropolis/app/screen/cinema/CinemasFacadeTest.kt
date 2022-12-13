package movie.metropolis.app.screen.cinema

import kotlinx.coroutines.test.runTest
import movie.core.model.Location
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class CinemasFacadeTest : FeatureTest() {

    private lateinit var facade: CinemasFacade

    override fun prepare() {
        facade = FacadeModule().cinemas(event)
    }

    @Test
    fun returns_listOfAll_withoutLocation() = runTest {
        whenever(event.getCinemas(null))
            .thenReturn(Result.success(List(13) { mock() }))
        val result = facade.getCinemas(null, null)
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().size == 13) { "Expected to contain 13 elements. ${result.getOrThrow()}" }
    }

    @Test
    fun returns_listOfLimited_withLocation() = runTest {
        whenever(event.getCinemas(Location(1.0, 1.0)))
            .thenReturn(Result.success(List(6) { mock() }))
        val result = facade.getCinemas(1.0, 1.0)
        assert(result.isSuccess) { result }
        assert(result.getOrThrow().size == 6) { "Expected to contain 6 elements. ${result.getOrThrow()}" }
    }

    @Test
    fun returns_failure() = runTest {
        whenever(event.getCinemas(Location(1.0, 1.0)))
            .thenReturn(Result.failure(RuntimeException()))
        val result = facade.getCinemas(1.0, 1.0)
        assert(result.isFailure) { result }
    }

}