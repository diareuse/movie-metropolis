package movie.style.modifier

import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.layout.*

fun Modifier.vertical(rotation: Float = 90f) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}.rotate(rotation)