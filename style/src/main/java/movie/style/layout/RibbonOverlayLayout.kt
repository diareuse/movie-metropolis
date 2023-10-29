package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.modifier.surface
import movie.style.modifier.surfacePoster
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import kotlin.math.pow
import kotlin.math.sqrt

@Composable
fun RibbonOverlayLayout(
    ribbon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    rotation: Float = -30f,
    color: Color = Theme.color.container.error,
    contentColor: Color = Theme.color.contentColorFor(color),
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current
    var size by remember { mutableStateOf(IntSize.Zero) }
    val maxWidth = remember(size, density) {
        with(density) {
            val a = size.height.toFloat().pow(2)
            val b = size.width.toFloat().pow(2)
            sqrt(a + b).toDp()
        }
    }
    Box(
        modifier = modifier.onGloballyPositioned { size = it.size },
        propagateMinConstraints = true
    ) {
        Box(
            modifier = Modifier.drawWithContent {
                drawContent()
                drawRect(Color.Black.copy(alpha = .3f))
            },
            propagateMinConstraints = true
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .wrapContentHeight()
                .requiredWidth(maxWidth)
                .align(Alignment.Center)
                .rotate(rotation)
                .surface(color),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                ribbon()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        RibbonOverlayLayout(
            modifier = Modifier
                .padding(24.dp)
                .height(200.dp)
                .aspectRatio(0.7f)
                .surfacePoster(),
            ribbon = { Text("Ribbon!", modifier = Modifier.padding(vertical = 4.dp)) }
        ) {
            Box(Modifier.fillMaxSize())
        }
    }
}