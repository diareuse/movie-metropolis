package movie.style

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.placeholder
import com.google.accompanist.placeholder.shimmer
import movie.style.theme.Theme

fun Modifier.imagePlaceholder(
    visible: Boolean
) = composed {
    imagePlaceholder(visible, Theme.container.button)
}

fun Modifier.imagePlaceholder(
    visible: Boolean,
    shape: Shape
) = composed {
    placeholder(
        visible = visible,
        color = MaterialTheme.colorScheme.surface,
        shape = shape,
        highlight = PlaceholderHighlight.shimmer(MaterialTheme.colorScheme.surfaceVariant)
    )
}

fun Modifier.textPlaceholder(
    visible: Boolean,
    color: Color = Color.Unspecified
) = composed {
    textPlaceholder(visible, Theme.container.buttonSmall, color)
}

fun Modifier.textPlaceholder(
    visible: Boolean,
    shape: Shape,
    color: Color = Color.Unspecified
) = composed {
    val background = LocalContentColor.current
    padding(if (visible) 2.dp else 0.dp).placeholder(
        visible = visible,
        color = background.copy(alpha = .1f),
        shape = shape,
        highlight = PlaceholderHighlight.shimmer(color.takeUnless { it == Color.Unspecified }
            ?: MaterialTheme.colorScheme.containerColorFor(background))
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