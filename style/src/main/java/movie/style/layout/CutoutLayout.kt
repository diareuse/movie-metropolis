package movie.style.layout

import androidx.compose.animation.*
import androidx.compose.animation.core.*
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
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

private val DpSize.Companion.VectorConverter
    get() = DpSizeToVector

private val DpSizeToVector = TwoWayConverter<DpSize, AnimationVector2D>(
    convertToVector = { AnimationVector2D(it.width.value, it.height.value) },
    convertFromVector = { DpSize(Dp(it.v1), Dp(it.v2)) }
)

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
    cutoutPadding: Dp = 8.dp,
    overlay: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        val (nextSize, setSize) = remember(overlay) { mutableStateOf(DpSize.Zero) }
        val size by animateValueAsState(
            targetValue = nextSize,
            typeConverter = DpSize.VectorConverter
        )
        val color by color.animate()
        val density = LocalDensity.current
        val shape = CompositeShape {
            setBaseline(contentShape)
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
                ),
            propagateMinConstraints = true
        ) {
            content()
        }
        Box(
            modifier = Modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .surface(color, cutoutShape, elevation, color)
                .animateContentSize()
                .onGloballyPositioned {
                    with(density) {
                        var size = DpSize(it.size.width.toDp(), it.size.height.toDp())
                        if (size != DpSize.Zero)
                            size += DpSize(cutoutPadding, cutoutPadding)
                        setSize(size)
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