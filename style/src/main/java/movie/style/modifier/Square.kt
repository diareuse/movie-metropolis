package movie.style.modifier

import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import kotlin.math.max

fun Modifier.square() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val dimension = max(placeable.height, placeable.width)
    layout(dimension, dimension) {
        if (placeable.width < dimension) {
            placeable.place((dimension - placeable.width) / 2, 0)
        } else {
            placeable.place(0, (dimension - placeable.height) / 2)
        }
    }
}