package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RegistrationViewModelTest : AbstractLoginViewModelTest() {

    override val mode: LoginMode
        get() = LoginMode.Registration

    @Test
    fun state_returnsLoaded_withData() = runTest {
        responder.onUrlRespond(UrlResponder.Register, "group-customer-service-customers.json")
        viewModel.send()
        val state = viewModel.state.first()
        assertEquals(true, state.loggedIn, state.toString())
        assertNull(state.error, state.toString())
    }

}