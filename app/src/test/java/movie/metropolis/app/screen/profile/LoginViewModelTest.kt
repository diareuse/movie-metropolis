package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.UrlResponder
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertIs

class LoginViewModelTest : ViewModelTest() {

    private lateinit var viewModel: LoginViewModel

    override fun prepare() {
        viewModel = LoginViewModel()
    }

    @Test
    fun state_returnsLoaded() = runTest {
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loaded<Boolean>>(loadable)
    }

    @Test
    fun state_returnsLoading() = runTest {
        viewModel.send()
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loading<Boolean>>(loadable)
    }

    @Test
    fun state_returnsError() = runTest {
        responder.delayBy(100)
        viewModel.send()
        advanceTimeBy(200)
        val loadable = viewModel.state.first()
        assertIs<Loadable.Error<Boolean>>(loadable)
    }

    @Test
    fun state_returnsLoaded_withData() = runTest {
        responder.onUrlRespond(UrlResponder.Auth, "group-customer-service-oauth-token.json")
        viewModel.send()
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loaded<Boolean>>(loadable)
        assert(loadable.result) { "Expected successful login" }
    }

}