package movie.metropolis.app.screen.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import movie.metropolis.app.screen.Loadable
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    val state: Flow<Loadable<Boolean>> = TODO()
    val username = MutableStateFlow("")
    val password = MutableStateFlow("")

    fun send() {
        TODO()
    }

}