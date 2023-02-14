package movie.metropolis.app.presentation.order

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.util.wheneverBlocking
import org.junit.Test
import org.mockito.internal.verification.NoInteractions
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    @Test
    fun isCompleted_returns_true() {
        facade.setUrl("https://sr.cinemacity.cz/S_CZ_1052/OrderCompletePageRes.aspx?dtticks=123456789&ec=123456")
        assertTrue { facade.isCompleted }
    }

    @Test
    fun isCompleted_returns_false() {
        facade.setUrl("https://sr.cinemacity.cz/S_CZ_1033/SelectTicketsPageRes.aspx?dtticks=123456789&ec=123456&hideBackButton=1")
        assertFalse { facade.isCompleted }
    }

    @Test
    fun isCompleted_returns_false_default() {
        assertFalse { facade.isCompleted }
    }

    @Test
    fun setUrl_notifiesListeners() = runTest {
        val listener = mock<OnChangedListener>()
        facade.addOnChangedListener(listener)
        facade.setUrl("")
        verify(listener).onChanged()
    }

    @Test
    fun setUrl_notifiesNoRemovedListeners() = runTest {
        val listener = mock<OnChangedListener>()
        facade.addOnChangedListener(listener)
        facade.removeOnChangedListener(listener)
        facade.setUrl("")
        verify(listener, NoInteractions()).onChanged()
    }

    // ---

    private fun token_responds_success(value: String): String {
        wheneverBlocking { credentials.getToken() }.thenReturn(Result.success(value))
        return value
    }

}