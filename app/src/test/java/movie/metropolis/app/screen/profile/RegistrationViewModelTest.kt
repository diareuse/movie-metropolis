package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import kotlin.test.assertIs

class RegistrationViewModelTest : AbstractLoginViewModelTest() {

    override val mode: LoginMode
        get() = LoginMode.Registration

    @Test
    fun state_returnsLoaded_withData() = runTest {
        responder.onUrlRespond(UrlResponder.Register, "group-customer-service-customers.json")
        viewModel.send()
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loaded<Boolean>>(loadable)
        assert(loadable.result) { "Expected successful registration" }
    }

}