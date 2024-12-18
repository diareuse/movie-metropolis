@file:Suppress("DEPRECATION")

package movie.style

import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import movie.style.theme.Theme

fun Modifier.imagePlaceholder(
    visible: Boolean = true
) = composed {
    imagePlaceholder(
        shape = Theme.container.button,
        visible = visible
    )
}

fun Modifier.imagePlaceholder(
    shape: Shape,
    visible: Boolean = true
) = composed {
    this
}

fun Modifier.textPlaceholder(
    visible: Boolean = true
) = composed {
    textPlaceholder(
        shape = Theme.container.buttonSmall,
        visible = visible
    )
}

fun Modifier.textPlaceholder(
    shape: Shape,
    visible: Boolean = true
) = composed {
    this
}
