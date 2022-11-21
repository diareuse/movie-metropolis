package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import kotlin.test.assertIs

class LoginViewModelTest : AbstractLoginViewModelTest() {

    override val mode: LoginMode
        get() = LoginMode.Login

    @Test
    fun state_returnsLoaded_withData() = runTest {
        responder.onUrlRespond(UrlResponder.Auth, "group-customer-service-oauth-token.json")
        viewModel.send()
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loaded<Boolean>>(loadable)
        assert(loadable.result) { "Expected successful login" }
    }

}