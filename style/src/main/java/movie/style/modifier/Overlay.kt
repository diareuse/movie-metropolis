package movie.style.modifier

import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
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