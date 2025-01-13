package movie.metropolis.app.ui.order

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderScreenScaffold(
    title: @Composable () -> Unit,
    website: @Composable (PaddingValues) -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
) = Scaffold(
    modifier = modifier,
    topBar = {
        LargeTopAppBar(
            modifier = Modifier
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
                .padding(1.pc)
                .clip(MaterialTheme.shapes.medium),
            windowInsets = WindowInsets(0),
            scrollBehavior = scrollBehavior,
            title = title,
            navigationIcon = navigationIcon
        )
    }
) { padding ->
    Box(
        modifier = Modifier.fillMaxSize(),
        propagateMinConstraints = true
    ) {
        website(padding)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun OrderScreenScaffoldPreview() = PreviewLayout {
    OrderScreenScaffold(
        title = { Text("Title") },
        website = { Box(Modifier.background(Color.Blue)) },
        navigationIcon = {
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        }
    )
}