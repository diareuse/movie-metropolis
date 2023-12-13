package movie.metropolis.app.screen.setup

import androidx.compose.runtime.*

@Immutable
data class LoginState(
    val email: String = "",
    val password: String = "",
    val error: Boolean = false,
    val loading: Boolean = false
)