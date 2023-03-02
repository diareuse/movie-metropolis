package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenLayout(
    profileIcon: @Composable () -> Unit = {},
    state: HomeScreenState = rememberHomeScreenState(),
    content: @Composable (PaddingValues, TopAppBarScrollBehavior) -> Unit
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    LaunchedEffect(state.title) {
        behavior.state.contentOffset = 0f
        behavior.state.heightOffset = 0f
    }
    Scaffold(
        topBar = {
            HomeScreenToolbar(
                profileIcon = profileIcon,
                behavior = behavior
            ) {
                AnimatedTitleText(target = state.titleResource()) {
                    if (it != null) Text(it)
                }
            }
        },
        content = { content(it, behavior) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenLayoutPreview() = PreviewLayout {
    HomeScreenLayout { _, _ -> }
}
