package movie.metropolis.app.screen.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import movie.metropolis.app.model.RegionView
import movie.metropolis.app.presentation.Posters
import movie.metropolis.app.presentation.login.LoginFacade
import movie.metropolis.app.presentation.setup.SetupFacade
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.regionsFlow
import movie.metropolis.app.presentation.setup.SetupFacade.Companion.requiresSetupFlow
import movie.metropolis.app.util.retainStateIn
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val facade: SetupFacade,
    private val login: LoginFacade
) : ViewModel() {

    val requiresSetup = facade.requiresSetupFlow
    val regions = facade.regionsFlow
        .map { it.getOrNull().orEmpty().toImmutableList() }
        .retainStateIn(viewModelScope, persistentListOf())

    val loginState = MutableStateFlow(LoginState())

    val posters = Posters.shuffled()

    fun select(view: RegionView) {
        viewModelScope.launch {
            facade.select(view)
        }
    }

    suspend fun login(): Result<Unit> {
        loginState.update { it.copy(loading = true, error = false) }
        val state = loginState.value
        return try {
            login.login(state.email, state.password).apply {
                loginState.update { it.copy(error = isFailure) }
            }
        } finally {
            loginState.update { it.copy(loading = false) }
        }
    }

}