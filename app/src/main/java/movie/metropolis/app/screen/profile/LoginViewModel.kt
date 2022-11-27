package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import movie.metropolis.app.feature.user.SignInMethod
import movie.metropolis.app.feature.user.UserFeature
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.StateMachine
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val feature: UserFeature
) : ViewModel() {

    private val stateMachine = StateMachine(viewModelScope, State()) {
        copy(loading = true, error = null)
    }

    val mode = MutableStateFlow(LoginMode.Login)

    val state = stateMachine.state
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val phone = MutableStateFlow("")

    fun send() {
        val method = when (mode.value) {
            LoginMode.Login -> SignInMethod.Login(email.value, password.value)
            LoginMode.Registration -> SignInMethod.Registration(
                email.value,
                password.value,
                firstName.value,
                lastName.value,
                phone.value
            )
        }

        stateMachine.submit {
            feature.signIn(method).fold(
                { copy(loading = false, loggedIn = true) },
                { copy(loading = false, loggedIn = false, error = it) }
            )
        }
    }

    data class State(
        val loading: Boolean = false,
        val loggedIn: Boolean = false,
        val error: Throwable? = null
    )


}