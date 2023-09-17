package movie.metropolis.app.screen.profile

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.login.LoginFacade
import javax.inject.Inject

@Stable
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val facade: LoginFacade,
) : ViewModel() {

    val mode = MutableStateFlow(LoginMode.Login)

    val domain = facade.domain
    val loading = MutableStateFlow(Loadable.success(Unit))
    val email = MutableStateFlow(facade.currentUserEmail.orEmpty())
    val password = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val phone = MutableStateFlow("")

    fun send(onSuccess: () -> Unit) = when (mode.value) {
        LoginMode.Login -> login(onSuccess)
        LoginMode.Registration -> register(onSuccess)
    }

    private fun login(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading.value = Loadable.loading()
            try {
                facade.login(email.value, password.value)
                loading.value = Loadable.success(Unit)
                onSuccess()
            } catch (e: Throwable) {
                loading.value = Loadable.failure(e)
            }
        }
    }

    private fun register(onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading.value = Loadable.loading()
            try {
                facade.register(
                    email.value,
                    password.value,
                    firstName.value,
                    lastName.value,
                    phone.value
                )
                loading.value = Loadable.success(Unit)
                onSuccess()
            } catch (e: Throwable) {
                loading.value = Loadable.failure(e)
            }
        }
    }

}