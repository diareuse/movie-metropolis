package movie.metropolis.app.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer

enum class ShapeToken {
    Small, Medium;

    val shape
        @Composable get() = when (this) {
            Small -> MaterialTheme.shapes.small
            Medium -> MaterialTheme.shapes.medium
        }
}

fun Modifier.imagePlaceholder(visible: Boolean, shape: ShapeToken = ShapeToken.Medium) = composed {
    placeholder(
        visible = visible,
        color = MaterialTheme.colorScheme.surface,
        shape = shape.shape,
        highlight = PlaceholderHighlight.shimmer(MaterialTheme.colorScheme.surfaceVariant)
    )
}

fun Modifier.textPlaceholder(visible: Boolean, shape: ShapeToken = ShapeToken.Small) = composed {
    val color = LocalContentColor.current
    padding(if (visible) 2.dp else 0.dp).placeholder(
        visible = visible,
        color = color.copy(alpha = .1f),
        shape = shape.shape,
        highlight = PlaceholderHighlight.shimmer(MaterialTheme.colorScheme.containerColorFor(color))
    )
}

private fun ColorScheme.containerColorFor(color: Color) = when (color) {
    onPrimary -> primary
    onSecondary -> secondary
    onTertiary -> tertiary
    onBackground -> background
    onError -> error
    onSurface -> surface
    onSurfaceVariant -> surfaceVariant
    onPrimaryContainer -> primaryContainer
    onSecondaryContainer -> secondaryContainer
    onTertiaryContainer -> tertiaryContainer
    onErrorContainer -> errorContainer
    inverseOnSurface -> inverseSurface
    else -> Color.Unspecified
}