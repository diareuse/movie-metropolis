package movie.metropolis.app.screen.profile

import kotlinx.coroutines.test.runTest
import movie.metropolis.app.di.FacadeModule
import movie.metropolis.app.screen.FeatureTest
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test

class LoginFacadeTest : FeatureTest() {

    private lateinit var facade: LoginFacade

    override fun prepare() {
        facade = FacadeModule().login(user)
    }

    @Test
    fun login_returns_success() = runTest {
        responder.onUrlRespond(UrlResponder.Auth, "group-customer-service-oauth-token.json")
        val result = facade.login("foo", "bar")
        assert(result.isSuccess) { result }
    }

    @Test
    fun login_returns_failure() = runTest {
        val result = facade.login("foo", "bar")
        assert(result.isFailure) { result }
    }

    @Test
    fun register_returns_success() = runTest {
        responder.onUrlRespond(UrlResponder.Register, "group-customer-service-customers.json")
        val result = facade.register("", "", "", "", "")
        assert(result.isSuccess) { result }
    }

    @Test
    fun register_returns_failure() = runTest {
        val result = facade.register("", "", "", "", "")
        assert(result.isFailure) { result }
    }

}