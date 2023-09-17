package movie.metropolis.app.screen.profile

import androidx.compose.animation.*
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Left
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.delay
import movie.metropolis.app.model.LoginMode
import movie.style.AppImage

@Composable
fun LoginScreen(
    onNavigateHome: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    Surface(modifier = Modifier.fillMaxSize()) {
        val urls by viewModel.posters.collectAsState()
        var index by remember { mutableStateOf(0) }
        AnimatedContent(
            targetState = index,
            transitionSpec = {
                slideIntoContainer(Left) togetherWith slideOutOfContainer(Left)
            }
        ) {
            val surface = MaterialTheme.colorScheme.surface
            AppImage(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.6f)
                    .drawWithContent {
                        val brush = Brush.radialGradient(
                            listOf(Color.Transparent, surface),
                            center = center.copy(y = 0f),
                            radius = size.height
                        )
                        drawContent()
                        drawRect(brush)
                    },
                url = urls.getOrNull(it) ?: return@AnimatedContent
            )
        }
        LaunchedEffect(index) {
            delay(5000)
            index = (index + 1) % urls.size
        }
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
}