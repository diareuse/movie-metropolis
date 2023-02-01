package movie.style.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
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
    Box(modifier.onGloballyPositioned { size = it.size }) {
        content()
        Box(
            modifier = Modifier
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