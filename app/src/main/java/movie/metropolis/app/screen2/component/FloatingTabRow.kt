package movie.metropolis.app.screen2.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import kotlin.math.hypot

@Composable
fun FloatingTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    tabs: @Composable () -> Unit,
) = TabRow(
    modifier = Modifier
        .clip(MaterialTheme.shapes.medium)
        .then(modifier),
    selectedTabIndex = selectedTabIndex,
    containerColor = Color.Transparent,
    contentColor = MaterialTheme.colorScheme.onSurface,
    tabs = tabs,
    divider = {},
    indicator = {}
)

@Composable
fun FloatingTab(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    selectedColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedColor: Color = MaterialTheme.colorScheme.onSurface,
    brushColors: List<Color> = listOf(
        MaterialTheme.colorScheme.primary,
        MaterialTheme.colorScheme.tertiary
    ),
    shape: Shape = MaterialTheme.shapes.small,
    label: @Composable () -> Unit
) = Box(
    modifier = modifier
        .fillMaxSize()
        .clickable(enabled = enabled, role = Role.Tab, onClick = onClick)
        .heightIn(min = 48.dp),
    contentAlignment = Alignment.Center
) {
    val alpha by animateFloatAsState(if (selected) 1f else 0f, tween(1000))
    val blur by animateIntAsState(if (selected) 0 else 32, tween(500))
    val scale by animateFloatAsState(if (selected) 1f else 2f, tween(500))
    val color by animateColorAsState(if (selected) selectedColor else unselectedColor, tween(1000))
    Spacer(
        modifier = Modifier
            .padding(8.dp)
            .matchParentSize()
            .scale(scale)
            .blur(blur.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
            .graphicsLayer {
                compositingStrategy = CompositingStrategy.Offscreen
                this.alpha = alpha
            }
            .drawBehind {
                val outline = shape.createOutline(size, layoutDirection, this)
                val brush = Brush.radialGradient(
                    colors = brushColors,
                    center = Offset(0f, size.height),
                    radius = hypot(size.width, size.height)
                )
                drawOutline(outline, brush)
            }
    )
    ProvideTextStyle(
        MaterialTheme.typography.labelMedium.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    ) {
        CompositionLocalProvider(LocalContentColor provides color) {
            label()
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun FloatingTabRowPreview() = PreviewLayout {
    var selected by remember { mutableIntStateOf(0) }
    FloatingTabRow(
        modifier = Modifier.background(Color.Gray),
        selectedTabIndex = selected
    ) {
        FloatingTab(
            selected = selected == 0,
            onClick = { selected = 0 }
        ) { Text("Movies", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        FloatingTab(
            selected = selected == 1,
            onClick = { selected = 1 }
        ) { Text("Upcoming", maxLines = 1, overflow = TextOverflow.Ellipsis) }
        FloatingTab(
            selected = selected == 2,
            onClick = { selected = 2 }
        ) { Text("Tickets", maxLines = 1, overflow = TextOverflow.Ellipsis) }
    }
}