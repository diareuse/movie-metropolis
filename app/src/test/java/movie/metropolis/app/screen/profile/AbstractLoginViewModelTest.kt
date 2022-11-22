package movie.metropolis.app.screen.profile

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.dropWhile
import kotlinx.coroutines.flow.first
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
        viewModel = LoginViewModel(SavedStateHandle(mapOf("mode" to mode.name)), user)
    }

    @Test
    fun state_returnsLoaded() = runTest {
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loaded<Boolean>>(loadable, loadable.toString())
    }

    @Test
    fun state_returnsLoading() = runTest {
        viewModel.send()
        val loadable = viewModel.state.first()
        assertIs<Loadable.Loading<Boolean>>(loadable, loadable.toString())
    }

    @Test
    fun state_returnsError() = runTest {
        viewModel.send()
        val loadable = viewModel.state.dropWhile { it is Loadable.Loading }.first()
        assertIs<Loadable.Error<Boolean>>(loadable, loadable.toString())
    }

}