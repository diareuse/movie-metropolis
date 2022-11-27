package movie.metropolis.app.screen.profile

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

abstract class AbstractLoginViewModelTest : ViewModelTest() {

    protected lateinit var viewModel: LoginViewModel
    abstract val mode: LoginMode

    override fun prepare() {
        viewModel = LoginViewModel(user)
    }

    @Test
    fun state_returnsLoaded() = runTest {
        val state = viewModel.state.first()
        assertEquals(LoginViewModel.State(), state, state.toString())
    }

    @Test
    fun state_returnsError() = runTest {
        viewModel.send()
        val state = viewModel.state.first()
        assertNotNull(state.error, state.toString())
    }

}