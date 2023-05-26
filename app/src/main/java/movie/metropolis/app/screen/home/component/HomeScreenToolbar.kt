package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.AppToolbar
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenToolbar(
    profileIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    behavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    title: @Composable () -> Unit
) {
    AppToolbar(
        modifier = modifier.background(Theme.color.container.background.copy(alpha = .9f)),
        title = title,
        navigationIcon = profileIcon,
        scrollBehavior = behavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenAppBarPreview() = PreviewLayout {
    HomeScreenToolbar(
        title = { Text("Foo") },
        profileIcon = {
            Box(
                Modifier
                    .size(24.dp)
                    .background(
                        Color.Magenta
                    )
            )
        }
    )
}
