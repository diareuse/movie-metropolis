package movie.style.layout

import androidx.compose.ui.unit.*

fun Constraints.minusWidth(width: Int): Constraints {
    val newMinWidth = (minWidth - width).coerceAtLeast(0)
    val newMaxWidth = (maxWidth - width).coerceAtLeast(newMinWidth)
    return copy(minWidth = newMinWidth, maxWidth = newMaxWidth)
}

fun Constraints.fixedWidth(width: Int = maxWidth) = copy(
    minWidth = width,
    maxWidth = maxWidth
)