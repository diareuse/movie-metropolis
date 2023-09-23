package movie.metropolis.app.screen.listing.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@Composable
fun CenterAlignedTabRow(
    selected: Int,
    modifier: Modifier = Modifier,
    indicator: @Composable () -> Unit = { RoundedIndicator() },
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Center,
    density: Density = LocalDensity.current,
    direction: LayoutDirection = LocalLayoutDirection.current,
    contentPadding: PaddingValues = PaddingValues(16.dp),
    tabs: @Composable () -> Unit
) {
    var indicatorOffset by rememberSaveable { mutableIntStateOf(0) }
    val offset by animateIntAsState(targetValue = indicatorOffset)
    SubcomposeLayout(
        modifier = modifier.padding(contentPadding),
        measurePolicy = { constraints ->
            val wrapConstraints = constraints.copy(minWidth = 0, minHeight = 0)
            val tabPlaceables = subcompose("tabs", tabs)
                .map { it.measure(wrapConstraints) }

            val width = tabPlaceables.sumOf { it.width }
            val height = tabPlaceables.maxOf { it.height }

            val selectedPlaceable = tabPlaceables[selected]
            val indicatorConstraints = Constraints.fixed(
                selectedPlaceable.width,
                selectedPlaceable.height
            )
            val indicatorPlaceable = subcompose("indicator", indicator).first()
                .measure(indicatorConstraints)
            indicatorOffset = tabPlaceables.takeWhile { it != selectedPlaceable }.sumOf { it.width }
            layout(width, height) {
                indicatorPlaceable.placeRelative(offset, 0)
                tabPlaceables
                    .getOffsets(verticalAlignment, horizontalArrangement, density, direction)
                    .forEachIndexed { index, offset ->
                        tabPlaceables[index].placeRelative(offset)
                    }
            }
        }
    )
}

@Composable
fun RoundedIndicator(modifier: Modifier = Modifier) {
    Box(modifier.background(Theme.color.content.background, CircleShape))
}

@Composable
fun Tab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    label: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clickable(role = Role.Tab, onClick = onClick)
            .padding(16.dp, 8.dp)
    ) {
        val color by animateColorAsState(
            targetValue = when (selected) {
                true -> Theme.color.container.background
                else -> Theme.color.content.background
            }
        )
        CompositionLocalProvider(
            LocalTextStyle provides Theme.textStyle.emphasis.copy(
                fontWeight = FontWeight.Medium,
                color = color
            ),
            LocalContentColor provides color
        ) {
            label()
        }
    }
}

private fun List<Placeable>.getOffsets(
    alignment: Alignment.Vertical,
    arrangement: Arrangement.Horizontal,
    density: Density,
    direction: LayoutDirection
): Sequence<IntOffset> {
    var height = 0
    var width = 0
    val widths = IntArray(size)
    for ((index, placeable) in withIndex()) {
        if (height < placeable.height) height = placeable.height
        width += placeable.width
        widths[index] = placeable.width
    }

    val x = IntArray(size)
    arrangement.run { density.arrange(width, widths, direction, x) }
    return x.asSequence().mapIndexed { index, x ->
        IntOffset(x, alignment.align(get(index).height, height))
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun CenterAlignedTabRowPreview() = Theme {
    var selected by rememberSaveable { mutableIntStateOf(0) }
    CenterAlignedTabRow(selected = selected) {
        Tab(selected = selected == 0, onClick = { selected = 0 }) {
            Text("First")
        }
        Tab(selected = selected == 1, onClick = { selected = 1 }) {
            Text("Second")
        }
    }
}
