package movie.metropolis.app.presentation.order

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import kotlin.test.assertEquals

class OrderFacadeTest : FeatureTest() {

    private lateinit var facade: OrderFacade

    override fun prepare() {
        facade = FacadeModule().order(credentials).create("url")
    }

    @Test
    fun getRequest_returns_url() = runTest {
        token_responds_success("token")
        val result = facade.getRequest().getOrThrow()
        assertEquals("url", result.url)
    }

    @Test
    fun getRequest_returns_headers() = runTest {
        val expected = token_responds_success("token")
        val result = facade.getRequest().getOrThrow()
        assertEquals(expected, result.headers["access-token"])
    }

    @Test
    fun getRequest_returns_noHeaders() = runTest {
        val result = facade.getRequest().getOrThrow()
        assertEquals(0, result.headers.size)
    }

    // ---

    private fun token_responds_success(value: String): String {
        wheneverBlocking { credentials.getToken() }.thenReturn(Result.success(value))
        return value
    }

}