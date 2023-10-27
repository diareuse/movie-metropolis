package ui.style.util

import androidx.compose.ui.geometry.*
import androidx.compose.ui.unit.*
import kotlin.math.roundToInt

fun DpSize.toSize(density: Density) = with(density) {
    Size(width = width.toPx(), height = height.toPx())
}

fun Size.toIntSize() =
    IntSize(width = width.roundToInt(), height = height.roundToInt())