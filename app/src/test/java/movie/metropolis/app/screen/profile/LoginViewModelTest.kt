package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.UrlResponder
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LoginViewModelTest : AbstractLoginViewModelTest() {

    override val mode: LoginMode
        get() = LoginMode.Login

    @Test
    fun state_returnsLoaded_withData() = runBlocking {
        responder.onUrlRespond(UrlResponder.Auth, "group-customer-service-oauth-token.json")
        viewModel.send()
        val state = viewModel.state.first()
        assertEquals(true, state.loggedIn, state.toString())
        assertNull(state.error, state.toString())
    }

}