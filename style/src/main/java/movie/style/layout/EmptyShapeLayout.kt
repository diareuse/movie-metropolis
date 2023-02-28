package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.modifier.stroke
import movie.style.theme.Theme

@Composable
fun EmptyShapeLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    shape: CornerBasedShape = Theme.container.card,
    color: Color = Theme.color.container.surface,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .stroke(color, shape)
            .padding(contentPadding),
        contentAlignment = contentAlignment
    ) {
        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        EmptyShapeLayout(contentPadding = PaddingValues(16.dp)) {
            Text("Hello")
        }
    }
}