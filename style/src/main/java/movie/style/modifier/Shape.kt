package movie.style.modifier

import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*

fun Modifier.clipWithGlow(
    shape: Shape,
    backgroundColor: Color,
    alpha: Float = .2f,
    width: Dp = 2.dp,
    lightSource: LightSource = LightSource.TopLeft
) = composed {
    val density = LocalDensity.current
    val color = LocalContentColor.current
    clip(shape).drawWithContent {
        val outline = shape.createOutline(size, layoutDirection, density)
        val width = with(density) { width.toPx() }
        val start = lightSource.start
        val end = lightSource.end
        val strokeColors =
            listOf(color.copy(alpha = alpha).compositeOver(backgroundColor), backgroundColor)
        val fillColors =
            listOf(color.copy(alpha = alpha / 2).compositeOver(backgroundColor), backgroundColor)
        val brush = Brush.linearGradient(strokeColors, start, end)
        val brush2 = Brush.linearGradient(fillColors, start, end)
        drawOutline(outline, brush2, style = Fill)
        drawContent()
        drawOutline(outline, brush, style = Stroke(width))
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