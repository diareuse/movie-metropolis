package movie.style.modifier

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme

fun Modifier.stroke(
    color: Color,
    shape: CornerBasedShape,
    width: Dp = 4.dp,
    stride: Dp = 4.dp
) = composed {
    val density = LocalDensity.current
    val direction = LocalLayoutDirection.current
    val strokeWidth = with(density) { width.toPx() }
    val strideSize = with(density) { stride.toPx() }
    drawWithContent {
        drawContent()
        drawOutline(
            outline = shape.createOutline(
                size = size,
                layoutDirection = direction,
                density = density
            ),
            color = color,
            style = Stroke(
                width = strokeWidth,
                miter = 0f,
                join = StrokeJoin.Round,
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(
                    floatArrayOf(strideSize - strokeWidth / 2, strideSize + strokeWidth),
                    0f
                )
            )
        )
    }.clip(shape)
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        Box(
            modifier = Modifier
                .size(128.dp)
                .padding(16.dp)
                .stroke(Color.Black, Theme.container.card)
        )
    }
}