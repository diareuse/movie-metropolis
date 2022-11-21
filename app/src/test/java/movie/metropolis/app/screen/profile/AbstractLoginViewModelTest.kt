package movie.metropolis.app.screen.profile

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.ViewModelTest
import org.junit.Test
import kotlin.test.assertIs

abstract class AbstractLoginViewModelTest : ViewModelTest() {

    protected lateinit var viewModel: LoginViewModel
    abstract val mode: LoginMode

    override fun prepare() {
        viewModel = LoginViewModel(SavedStateHandle(mapOf("mode" to mode.name)))
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

}