package movie.style.layout

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
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