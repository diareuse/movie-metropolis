package movie.style.util

import androidx.compose.ui.unit.*

fun IntSize.toDpSize(density: Density): DpSize {
    return with(density) { DpSize(width.toDp(), height.toDp()) }
}