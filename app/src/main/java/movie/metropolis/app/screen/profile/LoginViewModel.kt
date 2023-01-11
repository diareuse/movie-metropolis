package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.shareIn
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.profile.LoginFacade.Companion.stateFlow
import movie.metropolis.app.screen.retainStateIn
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    facade: LoginFacade,
) : ViewModel() {

    private val jobEmitter = Channel<suspend LoginFacade.() -> Result<Unit>>()
    private val jobEmitterFlow =
        jobEmitter.consumeAsFlow().shareIn(viewModelScope, SharingStarted.Eagerly, 1)

    val mode = MutableStateFlow(LoginMode.Login)

    val state = facade.stateFlow(jobEmitterFlow)
        .retainStateIn(viewModelScope, Loadable.success(false))
    val email = MutableStateFlow(facade.currentUserEmail.orEmpty())
    val password = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val phone = MutableStateFlow("")

    fun send() = when (mode.value) {
        LoginMode.Login -> login()
        LoginMode.Registration -> register()
    }.let {}

    private fun login() = jobEmitter.trySend {
        login(email.value, password.value)
    }

    private fun register() = jobEmitter.trySend {
        register(email.value, password.value, firstName.value, lastName.value, phone.value)
    }

}