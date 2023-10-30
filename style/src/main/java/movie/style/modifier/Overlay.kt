package movie.style.modifier

import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

fun Modifier.overlay() = composed {
    overlay(
        colorTop = Color.Transparent,
        colorBottom = Theme.color.container.background
    )
}

fun Modifier.overlay(
    colorBottom: Color,
    colorTop: Color = Color.Transparent
) = let {
    val colors = listOf(colorTop, colorBottom)
    val brush = Brush.verticalGradient(colors)
    drawWithContent {
        drawContent()
        drawRect(brush)
    }
}

fun Modifier.verticalOverlay(
    height: Dp,
    gravity: VerticalGravity = VerticalGravity.Top
) = composed {
    val color = Color.White
    val colors = listOf(color, color.copy(alpha = .3f), color.copy(alpha = .1f), Color.Transparent)
    val heightPx = with(LocalDensity.current) { height.toPx() }
    graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen).drawWithContent {
        val topLeft = gravity.getOffset(size, heightPx)
        val brushBounds = gravity.getBrushBounds(size, heightPx)
        val size = size.copy(height = heightPx)
        drawContent()
        val brush = Brush.verticalGradient(
            colors = colors,
            startY = brushBounds.start,
            endY = brushBounds.endInclusive
        )
        drawRect(brush, topLeft, size, blendMode = BlendMode.DstIn)
    }
}

enum class VerticalGravity {
    Top, Bottom;

    fun getOffset(size: Size, height: Float) = when (this) {
        Top -> Offset.Zero
        Bottom -> Offset(0f, size.height - height)
    }

    fun getBrushBounds(size: Size, height: Float): ClosedFloatingPointRange<Float> = when (this) {
        Top -> getOffset(size, height).run {
            y + height..y
        }

        Bottom -> getOffset(size, height).run {
            y..y + height
        }
    }
}