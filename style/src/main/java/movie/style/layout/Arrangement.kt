package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.ui.unit.*

fun Arrangement.Horizontal.arrange(
    density: Density,
    direction: LayoutDirection,
    fullSize: Int,
    vararg itemSizes: Int
): IntArray {
    return IntArray(itemSizes.size).apply {
        density.arrange(fullSize, itemSizes, direction, this)
    }
}