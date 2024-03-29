package movie.style.modifier

import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*

fun Modifier.glow(
    shape: Shape,
    color: Color = Color.Unspecified,
    lightSource: LightSource = LightSource.TopLeft,
    alpha: Float = .4f,
    fillAlpha: Float = alpha / 2f,
    width: Dp = 2.dp,
) = composed {
    val density = LocalDensity.current
    val contentColor = LocalContentColor.current
    drawWithCache {
        onDrawWithContent {
            val outline = shape.createOutline(size, layoutDirection, density)
            val width = with(density) { width.toPx() }
            val color = color.takeOrElse { contentColor }
            val start = lightSource.start
            val end = lightSource.end
            drawContent()
            if (fillAlpha > 0) {
                val fillColors =
                    listOf(color.copy(alpha = fillAlpha), Color.Transparent)
                val brush2 = Brush.linearGradient(fillColors, start, end)
                drawOutline(outline, brush2, style = Fill)
            }
            if (width > 0) {
                val strokeColors =
                    listOf(color.copy(alpha = alpha), Color.Transparent)
                val brush = Brush.linearGradient(strokeColors, start, end)
                drawOutline(outline, brush, style = Stroke(width))
            }
        }
    }
}

enum class LightSource(val start: Offset, val end: Offset) {
    TopLeft(Offset.Zero, Offset.Infinite),
    Top(Offset.Zero, Offset(0f, Float.POSITIVE_INFINITY)),
    TopRight(Offset(Float.POSITIVE_INFINITY, 0f), Offset(0f, Float.POSITIVE_INFINITY)),
    Right(Offset(Float.POSITIVE_INFINITY, 0f), Offset.Zero),
    BottomRight(Offset.Infinite, Offset.Zero),
    Bottom(Offset(0f, Float.POSITIVE_INFINITY), Offset.Zero),
    BottomLeft(Offset(0f, Float.POSITIVE_INFINITY), Offset(Float.POSITIVE_INFINITY, 0f)),
    Left(Offset.Zero, Offset(Float.POSITIVE_INFINITY, 0f))
}