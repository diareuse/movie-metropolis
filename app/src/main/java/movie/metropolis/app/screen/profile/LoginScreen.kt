package movie.metropolis.app.screen.profile

import androidx.compose.runtime.*
import movie.metropolis.app.model.LoginMode
import movie.metropolis.app.screen.profile.component.AnimatedImageBackground

@Composable
fun LoginScreen(
    mode: LoginMode,
    urls: List<String>,
    email: String,
    password: String,
    error: Boolean,
    loading: Boolean,
    domain: String,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackClick: () -> Unit
) = AnimatedImageBackground(urls = urls) {
    when (mode) {
        LoginMode.Login -> LoginSignInScreen(
            email = email,
            password = password,
            error = error,
            loading = loading,
            domain = domain,
            onEmailChanged = onEmailChanged,
            onPasswordChanged = onPasswordChanged,
            onSendClick = onSendClick,
            onBackClick = onBackClick
        )

        LoginMode.Registration -> LoginRegistrationScreen()
    }
}