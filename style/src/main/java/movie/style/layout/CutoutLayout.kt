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
import movie.style.shape.CompositeShape
import movie.style.shape.CutoutShape
import movie.style.state.animate
import movie.style.state.smartAnimate
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

@Composable
fun CutoutLayout(
    color: Color,
    shape: Shape,
    modifier: Modifier = Modifier,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit
) = CutoutLayout(
    color = color,
    cutoutShape = shape,
    contentShape = shape,
    modifier = modifier,
    overlay = overlay,
    content = content
)

@Composable
fun CutoutLayout(
    color: Color,
    cutoutShape: Shape,
    contentShape: Shape,
    modifier: Modifier = Modifier,
    elevation: Dp = 16.dp,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        val (nextWidth, setWidth) = remember(overlay) { mutableStateOf(0.dp) }
        val (nextHeight, setHeight) = remember(overlay) { mutableStateOf(0.dp) }
        val width by nextWidth.smartAnimate()
        val height by nextHeight.smartAnimate()
        val color by color.animate()
        val density = LocalDensity.current
        val shape = CompositeShape {
            setBaseline(contentShape)
            val size = DpSize(width, height) + DpSize(8.dp, 8.dp)
            addShape(
                shape = CutoutShape(CornerSize(24.dp), CutoutShape.Orientation.TopRight),
                size = size,
                alignment = Alignment.TopEnd,
                operation = PathOperation.Difference
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .surface(
                    color = Theme.color.container.background,
                    shape = shape,
                    elevation = elevation,
                    shadowColor = color
                )
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .surface(color, cutoutShape, elevation, color)
                .animateContentSize()
                .onGloballyPositioned {
                    with(density) {
                        setWidth(it.size.width.toDp())
                        setHeight(it.size.height.toDp())
                    }
                }
                .align(Alignment.TopEnd)
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