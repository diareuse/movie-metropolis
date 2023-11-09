@file:Suppress("DEPRECATION")

package movie.style

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.shimmer
import com.google.accompanist.placeholder.placeholder
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
    placeholder(
        visible = visible,
        color = Theme.color.container.background,
        shape = shape,
        highlight = PlaceholderHighlight.shimmer()
    )
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
    val background = LocalContentColor.current
    padding(
        vertical = if (visible) 1.dp else 0.dp
    ).placeholder(
        visible = visible,
        color = background.copy(alpha = .1f),
        shape = shape
    )
}
