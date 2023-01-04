package movie.metropolis.app.screen.order

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class OrderFacadeTest : FeatureTest() {

    private lateinit var facade: OrderFacade

    override fun prepare() {
        facade = FacadeModule().order(user).create("url")
    }

    @Test
    fun getRequest_returns_url() = runTest {
        whenever(user.getToken()).thenReturn(Result.success("token"))
        val result = facade.getRequest().getOrThrow()
        assertEquals("url", result.url)
    }

    @Test
    fun getRequest_returns_headers() = runTest {
        whenever(user.getToken()).thenReturn(Result.success("token"))
        val result = facade.getRequest().getOrThrow()
        assertEquals("token", result.headers["access-token"])
    }

}