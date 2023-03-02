package movie.metropolis.app.screen.home.component

import androidx.compose.runtime.*
import androidx.compose.ui.res.*

@Stable
class HomeScreenState {

    var title by mutableStateOf(0)

    @Composable
    fun titleResource() = if (title == 0) null else stringResource(title)

}

@Composable
fun rememberHomeScreenState() = remember {
    HomeScreenState()
}