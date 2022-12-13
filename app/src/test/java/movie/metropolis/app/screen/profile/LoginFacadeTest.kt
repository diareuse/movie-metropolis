package movie.metropolis.app.screen.profile

import kotlinx.coroutines.test.runTest
import movie.core.model.SignInMethod
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import org.junit.Test
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class LoginFacadeTest : FeatureTest() {

    private lateinit var facade: LoginFacade

    override fun prepare() {
        facade = FacadeModule().login(user)
    }

    @Test
    fun login_callsLogin() = runTest {
        facade.login("foo", "bar")
        verify(user).signIn(SignInMethod.Login("foo", "bar"))
    }

    @Test
    fun login_returns_success() = runTest {
        whenever(user.signIn(SignInMethod.Login("foo", "bar")))
            .thenReturn(Result.success(Unit))
        val result = facade.login("foo", "bar")
        assert(result.isSuccess) { result }
    }

    @Test
    fun login_returns_failure() = runTest {
        whenever(user.signIn(SignInMethod.Login("foo", "bar")))
            .thenReturn(Result.failure(RuntimeException()))
        val result = facade.login("foo", "bar")
        assert(result.isFailure) { result }
    }

    @Test
    fun register_callsRegistration() = runTest {
        facade.register("", "", "", "", "")
        verify(user).signIn(SignInMethod.Registration("", "", "", "", ""))
    }

    @Test
    fun register_returns_success() = runTest {
        whenever(user.signIn(SignInMethod.Registration("", "", "", "", "")))
            .thenReturn(Result.success(Unit))
        val result = facade.register("", "", "", "", "")
        assert(result.isSuccess) { result }
    }

    @Test
    fun register_returns_failure() = runTest {
        whenever(user.signIn(SignInMethod.Registration("", "", "", "", "")))
            .thenReturn(Result.failure(RuntimeException()))
        val result = facade.register("", "", "", "", "")
        assert(result.isFailure) { result }
    }

}