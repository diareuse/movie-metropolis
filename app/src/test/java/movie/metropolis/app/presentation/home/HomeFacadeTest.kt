package movie.metropolis.app.presentation.home

import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import org.junit.Test
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

class HomeFacadeTest : FeatureTest() {

    private lateinit var facade: HomeFacade

    override fun prepare() {
        facade = FacadeModule().home(credentials)
    }

    @Test
    fun email_returns() {
        val email = "foo@foo.com"
        whenever(credentials.email).thenReturn(email)
        assertEquals(email, facade.email)
    }

    @Test
    fun email_returns_null() {
        assertNull(facade.email)
    }

}