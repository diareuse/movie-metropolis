package movie.style.util

import androidx.compose.ui.geometry.*
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

fun IntSize.toDpSize(density: Density): DpSize {
    return with(density) { DpSize(width.toDp(), height.toDp()) }
}

fun DpSize.toSize(density: Density) = with(density) {
    Size(width = width.toPx(), height = height.toPx())
}

fun Size.toIntSize() =
    IntSize(width = width.roundToInt(), height = height.roundToInt())

fun Offset.toDpOffset(density: Density) = with(density) { DpOffset(x.toDp(), y.toDp()) }