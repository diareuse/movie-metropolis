@file:OptIn(ExperimentalMaterial3Api::class)

package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.*
import movie.style.layout.PreviewLayout
import movie.style.layout.arrange
import movie.style.layout.fixedWidth
import movie.style.layout.minusWidth
import movie.style.layout.plus
import movie.style.theme.Theme
import kotlin.math.max

private const val NavigationIcon = "navigationIcon"
private const val Title = "title"
private const val Actions = "actions"
private const val ExpandedTitle = "expandedTitle"

@Composable
fun CollapsingTopAppBar(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    minHeight: Dp = 48.dp,
    arrangement: Arrangement.Horizontal = Arrangement.spacedBy(8.dp),
    contentPadding: PaddingValues = PaddingValues(12.dp),
    expandedTitlePadding: PaddingValues = PaddingValues(start = 12.dp, top = 24.dp),
    titleStyle: TextStyle = Theme.textStyle.title,
    expandedTextStyle: TextStyle = titleStyle.copy(fontSize = titleStyle.fontSize * 2)
) {
    val alignment = Alignment.CenterVertically
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val expandedTitlePaddingStart = with(density) {
        expandedTitlePadding.calculateStartPadding(direction).roundToPx()
    }
    val expandedTitlePaddingTop = with(density) {
        expandedTitlePadding.calculateTopPadding().roundToPx()
    }
    val fraction = scrollBehavior.state.collapsedFraction
    val expandFraction = fraction.coerceAtMost(.5f) * 2
    val hideFraction = (fraction - .5f).coerceIn(0f, .5f) * 2
    val expandedTitleStyle = lerp(expandedTextStyle, titleStyle, expandFraction)
    val spacing = with(density) { arrangement.spacing.roundToPx() }
    val minHeight = with(density) { minHeight.roundToPx() }
    Layout(
        modifier = modifier
            .padding(contentPadding)
            .statusBarsPadding(),
        content = {
            Box(Modifier.layoutId(NavigationIcon)) { navigationIcon() }
            Box(Modifier.layoutId(Title)) {
                ProvideTextStyle(titleStyle) {
                    title()
                }
            }
            Box(Modifier.layoutId(Actions)) { actions() }
            Box(Modifier.layoutId(ExpandedTitle)) {
                ProvideTextStyle(expandedTitleStyle) {
                    title()
                }
            }
        }
    ) { measurables, constraints ->
        // Stage 1: Measure
        var cs = constraints.copy(minWidth = 0, minHeight = 0)
        cs = cs.minusWidth(spacing)
        val navigationIcon = measurables.first { it.layoutId == NavigationIcon }.measure(cs)
        cs = cs.minusWidth(navigationIcon.width)
        val actions = measurables.first { it.layoutId == Actions }.measure(cs)
        cs = cs.minusWidth(actions.width).fixedWidth()
        val title = measurables.first { it.layoutId == Title }.measure(cs)
        val expandedTitle = measurables.first { it.layoutId == ExpandedTitle }.measure(constraints)

        // Stage 2: Arrange
        val pinnedHeight = maxOf(navigationIcon.height, actions.height, title.height, minHeight)
        val (offsetNavigationIcon, offsetTitle, offsetActions) = arrangement.arrange(
            density = density,
            direction = direction,
            fullSize = constraints.maxWidth,
            navigationIcon.width, title.width, actions.width
        )
        val expandedX = lerp(
            start = offsetNavigationIcon + expandedTitlePaddingStart,
            stop = offsetTitle,
            fraction = expandFraction
        )
        val expandedY = lerp(
            start = pinnedHeight + expandedTitlePaddingTop,
            stop = alignment.align(title.height, pinnedHeight),
            fraction = expandFraction
        )

        // Stage 3: Modify scroll behavior
        val height = max(pinnedHeight, expandedY + expandedTitle.height)
        if (scrollBehavior.state.heightOffsetLimit != -height.toFloat()) {
            scrollBehavior.state.heightOffsetLimit = -height.toFloat()
        }

        // Stage 4: Layout
        val offset = (height * hideFraction).toInt().coerceAtLeast(0)
        layout(constraints.maxWidth, height - offset) {
            navigationIcon.placeRelative(
                x = offsetNavigationIcon,
                y = alignment.align(navigationIcon.height, pinnedHeight) - offset
            )
            actions.placeRelative(
                x = offsetActions,
                y = alignment.align(actions.height, pinnedHeight) - offset
            )
            expandedTitle.placeRelative(
                x = expandedX,
                y = expandedY - offset
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun CollapsingTopAppBarPreview() = PreviewLayout(padding = PaddingValues()) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    SideEffect {
        behavior.state.heightOffset = -170f
    }
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                scrollBehavior = behavior,
                title = { Text("I'm title") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.MoreVert, null)
                    }
                }
            )
        }
    ) { padding ->
        SampleContent(padding = padding, behavior = behavior)
    }
}

@Preview(showBackground = true)
@Composable
private fun CollapsingTopAppBarTitleOnlyPreview() = PreviewLayout(padding = PaddingValues()) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    SideEffect {
        behavior.state.heightOffset = -170f
    }
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                scrollBehavior = behavior,
                title = { Text("I'm title") }
            )
        }
    ) { padding ->
        SampleContent(padding = padding, behavior = behavior)
    }
}

@Preview(showBackground = true)
@Composable
private fun CollapsingTopAppBarExpandedPreview() = PreviewLayout(padding = PaddingValues()) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            CollapsingTopAppBar(
                scrollBehavior = behavior,
                title = { Text("I'm title") },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.AutoMirrored.Default.ArrowBack, null)
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Default.MoreVert, null)
                    }
                }
            )
        }
    ) { padding ->
        SampleContent(padding = padding, behavior = behavior)
    }
}

@Composable
fun SampleContent(padding: PaddingValues, behavior: TopAppBarScrollBehavior) {
    LazyColumn(
        modifier = Modifier.nestedScroll(behavior.nestedScrollConnection),
        contentPadding = padding.plus(24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(100) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .background(Color.Blue)
            )
        }
    }
}