package movie.metropolis.app.ui.setup

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import movie.metropolis.app.presentation.login.LoginFacade
import movie.metropolis.app.presentation.setup.SetupFacade
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.requiresSetupFlow
import javax.inject.Inject

@Stable
@HiltViewModel
class SetupViewModel @Inject constructor(
    private val facade: SetupFacade,
    private val login: LoginFacade
) : ViewModel() {

    val requiresSetup = facade.requiresSetupFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(),
        facade.requiresSetup
    )
    val state = SetupViewState()

    fun login() = viewModelScope.async {
        state.error = false
        state.loading = true
        try {
            facade.select(state.region ?: return@async false)
            login.login(state.email, state.password).getOrThrow()
            return@async true
        } catch (e: Throwable) {
            state.error = true
        } finally {
            state.loading = false
        }
        return@async false
    }

}