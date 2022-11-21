package movie.metropolis.app.screen.profile

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.Loadable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    state: SavedStateHandle
) : ViewModel() {

    val mode: LoginMode = LoginMode.valueOf(state.get<String>("mode").let(::requireNotNull))

    val state: Flow<Loadable<Boolean>> = TODO()
    val email = MutableStateFlow("")
    val password = MutableStateFlow("")
    val firstName = MutableStateFlow("")
    val lastName = MutableStateFlow("")
    val phone = MutableStateFlow("")

    fun send() {
        TODO()
    }

}