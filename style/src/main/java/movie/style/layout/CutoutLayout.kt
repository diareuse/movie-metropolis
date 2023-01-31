package movie.style.layout

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import movie.style.modifier.surface
import movie.style.theme.Theme
import movie.style.theme.extendBy

@Composable
fun CutoutLayout(
    color: Color,
    shape: CornerBasedShape,
    modifier: Modifier = Modifier,
    cutoutPadding: Dp = 8.dp,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        var width by remember { mutableStateOf(0.dp) }
        var height by remember { mutableStateOf(0.dp) }
        val density = LocalDensity.current
        val elevation = 16.dp
        val color = animateColorAsState(color).value
        val containerShape =
            if (width <= 0.dp || height <= 0.dp) shape.extendBy(cutoutPadding)
            else CutoutShape.from(
                shape.extendBy(cutoutPadding),
                width + cutoutPadding,
                height + cutoutPadding
            )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .surface(Theme.color.container.background, containerShape, elevation, color)
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .onGloballyPositioned {
                    with(density) {
                        width = it.size.width.toDp()
                        height = it.size.height.toDp()
                    }
                }
                .align(Alignment.TopEnd)
                .surface(color, shape, elevation, color)
        ) {
            overlay()
        }
    }
}