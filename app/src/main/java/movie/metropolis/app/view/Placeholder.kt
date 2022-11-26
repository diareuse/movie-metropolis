package movie.metropolis.app.view

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
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
    padding(if (visible) 2.dp else 0.dp).placeholder(
        visible = visible,
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = shape.shape,
        highlight = PlaceholderHighlight.shimmer(MaterialTheme.colorScheme.background)
    )
}