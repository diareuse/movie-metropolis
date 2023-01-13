package movie.metropolis.app.screen.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.model.LoginMode

@Composable
fun LoginScreen(
    onNavigateHome: () -> Unit,
    onBackClick: () -> Unit,
    onLinkClick: (String) -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    when (viewModel.mode.collectAsState().value) {
        LoginMode.Login -> LoginSignInScreen(
            viewModel = viewModel,
            onNavigateHome = onNavigateHome,
            onBackClick = onBackClick,
            onLinkClick = onLinkClick
        )

        LoginMode.Registration -> LoginRegistrationScreen(
            viewModel,
            onNavigateHome = onNavigateHome
        )
    }
}