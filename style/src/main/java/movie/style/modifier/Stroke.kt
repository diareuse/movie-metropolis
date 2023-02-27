package movie.style.modifier

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
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