package movie.metropolis.app.ui.booking

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.util.pc

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenScaffold(
    filters: @Composable ColumnScope.() -> Unit,
    backdrop: @Composable () -> Unit,
    title: @Composable () -> Unit,
    activeFilters: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scaffoldState: BottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden,
            skipHiddenState = false
        )
    ),
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    content: @Composable (PaddingValues) -> Unit,
) = BottomSheetScaffold(
    modifier = modifier,
    sheetContent = filters,
    scaffoldState = scaffoldState
) { padding ->
    Box {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .alpha(.25f)
                .blur(1.pc),
            propagateMinConstraints = true
        ) {
            backdrop()
        }
        var height by remember { mutableStateOf(0.dp) }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            propagateMinConstraints = true
        ) {
            content(padding + PaddingValues(top = height))
        }
        val density = LocalDensity.current
        LargeTopAppBar(
            modifier = Modifier
                .onSizeChanged { height = with(density) { it.height.toDp() } }
                .windowInsetsPadding(
                    WindowInsets.systemBars.only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                )
                .padding(1.pc)
                .clip(MaterialTheme.shapes.medium),
            windowInsets = WindowInsets(0),
            title = title,
            scrollBehavior = scrollBehavior,
            navigationIcon = navigationIcon,
            actions = {
                BadgedBox(badge = { activeFilters() }) {
                    val scope = rememberCoroutineScope()
                    IconButton({
                        scope.launch {
                            scaffoldState.bottomSheetState.show()
                        }
                    }) {
                        Icon(Icons.Default.MoreVert, null)
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@PreviewLightDark
@PreviewFontScale
@Composable
private fun BookingScreenScaffoldPreview() = PreviewLayout {
    BookingScreenScaffold(
        filters = {},
        backdrop = { Box(Modifier.background(Color.Green)) },
        title = { Text("I am booking") },
        content = { Box(Modifier
            .padding(it)
            .background(Color.Blue)) },
        activeFilters = { Badge { Text("3") } },
        navigationIcon = {
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        }
    )
}