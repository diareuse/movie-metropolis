package movie.metropolis.app.screen.profile

import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.model.LoginMode

@Composable
fun LoginScreen(
    onNavigateHome: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    when (viewModel.mode.collectAsState().value) {
        LoginMode.Login -> LoginSignInScreen(
            viewModel = viewModel,
            onNavigateHome = onNavigateHome,
            onBackClick = onBackClick
        )

        LoginMode.Registration -> LoginRegistrationScreen(
            viewModel,
            onNavigateHome = onNavigateHome
        )
    }
}