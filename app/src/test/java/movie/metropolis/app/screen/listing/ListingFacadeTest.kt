package movie.metropolis.app.screen.listing

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class ListingFacadeTest : FeatureTest() {

    private lateinit var facade: ListingFacade

    override fun prepare() {
        facade = FacadeModule().listing(event)
    }

    @Test
    fun returns_current_success() = runTest {
        whenever(event.getCurrent()).thenReturn(Result.success(List(34) { mock() }))
        val result = facade.getCurrent()
        assert(result.isSuccess) { result }
        assertEquals(34, result.getOrThrow().size)
    }

    @Test
    fun returns_current_failure() = runTest {
        whenever(event.getCurrent()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getCurrent()
        assert(result.isFailure) { result }
    }

    @Test
    fun returns_upcoming_success() = runTest {
        whenever(event.getUpcoming()).thenReturn(Result.success(List(18) { mock() }))
        val result = facade.getUpcoming()
        assert(result.isSuccess) { result }
        assertEquals(18, result.getOrThrow().size)
    }

    @Test
    fun returns_upcoming_failure() = runTest {
        whenever(event.getUpcoming()).thenReturn(Result.failure(RuntimeException()))
        val result = facade.getUpcoming()
        assert(result.isFailure) { result }
    }

}