package movie.style.layout

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import movie.style.modifier.surface
import movie.style.state.animate
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import movie.style.theme.extendBy

@Composable
fun CutoutLayout(
    color: Color,
    shape: CornerBasedShape,
    modifier: Modifier = Modifier,
    cutoutPadding: Dp = 8.dp,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit
) = CutoutLayout(
    color = color,
    cutoutShape = shape,
    contentShape = shape,
    modifier = modifier,
    cutoutPadding = cutoutPadding,
    overlay = overlay,
    content = content
)

@Composable
fun CutoutLayout(
    color: Color,
    cutoutShape: CornerBasedShape,
    contentShape: CornerBasedShape,
    modifier: Modifier = Modifier,
    cutoutPadding: Dp = 8.dp,
    elevation: Dp = 16.dp,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        val (nextWidth, setWidth) = remember { mutableStateOf(0.dp) }
        val (nextHeight, setHeight) = remember { mutableStateOf(0.dp) }
        val width = nextWidth.animate()
        val height = nextHeight.animate()
        val density = LocalDensity.current
        val color = color.animate()
        val expandedShape = contentShape.extendBy(cutoutPadding)
        val containerShape = remember(width, height) {
            if (width <= 0.dp || height <= 0.dp) contentShape
            else CutoutShape.from(
                contentShape,
                expandedShape,
                width + cutoutPadding,
                height + cutoutPadding
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .surface(
                    color = Theme.color.container.background,
                    shape = IntersectShape(contentShape, containerShape),
                    elevation = elevation,
                    shadowColor = color
                )
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .animateContentSize()
                .onGloballyPositioned {
                    with(density) {
                        setWidth(it.size.width.toDp())
                        setHeight(it.size.height.toDp())
                    }
                }
                .align(Alignment.TopEnd)
                .surface(color, cutoutShape, elevation, color)
        ) {
            CompositionLocalProvider(
                LocalContentColor provides Theme.color.contentColorFor(color),
                LocalTextStyle provides Theme.textStyle.emphasis
            ) {
                overlay()
            }
        }
    }
}