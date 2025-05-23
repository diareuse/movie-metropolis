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
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch
import movie.style.layout.PreviewLayout
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
    haze: HazeState = remember { HazeState() },
    content: @Composable (PaddingValues) -> Unit,
) = Scaffold(
    modifier = modifier,
    topBar = {
        Row(
            modifier = Modifier
                .padding(2.pc)
                .statusBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.medium)
                    .hazeEffect(haze)
            ) {
                navigationIcon()
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 2.pc),
                contentAlignment = Alignment.Center
            ) {
                ProvideTextStyle(
                    MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                ) {
                    title()
                }
            }
            BadgedBox(badge = { activeFilters() }) {
                Row(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.medium)
                        .hazeEffect(haze)
                ) {
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
        }
    },
    bottomBar = {}
) { padding ->
    Box {
        val color = MaterialTheme.colorScheme.background
        Box(
            modifier = Modifier
                .hazeSource(haze)
                .fillMaxSize()
                .blur(1.pc)
                .drawWithCache {
                    val brush = Brush.radialGradient(
                        listOf(Color.Transparent, color),
                        center = Offset.Zero,
                        radius = size.maxDimension
                    )
                    onDrawWithContent {
                        drawContent()
                        drawRect(brush)
                    }
                },
            propagateMinConstraints = true
        ) {
            backdrop()
        }
        content(padding)
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
        content = {
            Box(
                Modifier
                    .padding(2.pc)
                    .background(Color.Blue)
            )
        },
        activeFilters = { Badge { Text("3") } },
        navigationIcon = {
            IconButton({}) {
                Icon(Icons.AutoMirrored.Default.ArrowBack, null)
            }
        }
    )
}