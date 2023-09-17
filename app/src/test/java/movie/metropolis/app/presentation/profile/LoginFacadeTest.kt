package movie.metropolis.app.presentation.profile

import kotlinx.coroutines.test.runTest
import movie.core.model.Region
import movie.core.model.SignInMethod
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.presentation.FeatureTest
import movie.metropolis.app.presentation.login.LoginFacade
import org.junit.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginFacadeTest : FeatureTest() {

    private lateinit var facade: LoginFacade

    override fun prepare() {
        facade = FacadeModule().login(credentials, setup, poster)
    }

    @Test
    fun login_callsLogin() = runTest {
        facade.login("foo", "bar")
        verify(credentials).signIn(SignInMethod.Login("foo", "bar"))
    }

    @Test
    fun login_returns_success() = runTest {
        whenever(credentials.signIn(SignInMethod.Login("foo", "bar")))
            .thenReturn(Result.success(Unit))
        val result = facade.login("foo", "bar")
        assert(result.isSuccess) { result }
    }

    @Test
    fun login_returns_failure() = runTest {
        whenever(credentials.signIn(SignInMethod.Login("foo", "bar")))
            .thenReturn(Result.failure(RuntimeException()))
        val result = facade.login("foo", "bar")
        assert(result.isFailure) { result }
    }

    @Test
    fun register_callsRegistration() = runTest {
        facade.register("", "", "", "", "")
        verify(credentials).signIn(SignInMethod.Registration("", "", "", "", ""))
    }

    @Test
    fun register_returns_success() = runTest {
        whenever(credentials.signIn(SignInMethod.Registration("", "", "", "", "")))
            .thenReturn(Result.success(Unit))
        val result = facade.register("", "", "", "", "")
        assert(result.isSuccess) { result }
    }

    @Test
    fun register_returns_failure() = runTest {
        whenever(credentials.signIn(SignInMethod.Registration("", "", "", "", "")))
            .thenReturn(Result.failure(RuntimeException()))
        val result = facade.register("", "", "", "", "")
        assert(result.isFailure) { result }
    }

    @Test
    fun currentUserEmail_returns_value() {
        whenever(credentials.email).thenReturn("email")
        assertEquals("email", facade.currentUserEmail)
    }

    @Test
    fun currentUserEmail_returns_null() {
        assertNull(facade.currentUserEmail)
    }

    @Test
    fun domain_returns_currentDomain() {
        whenever(setup.region).thenReturn(Region.Custom("foo", 0))
        assertEquals("foo", facade.domain)
    }

}