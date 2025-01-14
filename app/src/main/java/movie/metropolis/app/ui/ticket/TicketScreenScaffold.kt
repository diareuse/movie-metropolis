package movie.metropolis.app.ui.ticket

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
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
fun TicketScreenScaffold(
    state: PagerState,
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
    item: @Composable (index: Int) -> Unit,
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
    HorizontalPager(
        modifier = Modifier.fillMaxSize(),
        state = state,
        contentPadding = padding,
        pageNestedScrollConnection = scrollBehavior.nestedScrollConnection
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            propagateMinConstraints = true
        ) {
            item(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun TicketScreenScaffoldPreview() = PreviewLayout {
    TicketScreenScaffold(
        state = rememberPagerState { 1 },
        title = { Text("Tickets") },
        navigationIcon = {
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        }
    ) { index ->
        Box(Modifier.background(Color.Green))
    }
}