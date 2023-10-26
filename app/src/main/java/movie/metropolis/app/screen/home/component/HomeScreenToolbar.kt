package movie.metropolis.app.screen.home.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import movie.style.CollapsingTopAppBar
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenToolbar(
    modifier: Modifier = Modifier,
    behavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    title: @Composable () -> Unit
) {
    CollapsingTopAppBar(
        modifier = modifier,
        scrollBehavior = behavior,
        title = title
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun HomeScreenAppBarPreview() = PreviewLayout {
    HomeScreenToolbar(
        title = { Text("Foo") }
    )
}
