package movie.style.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.style.modifier.stroke
import movie.style.theme.Theme

@Composable
fun EmptyShapeLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    shape: CornerBasedShape = Theme.container.card,
    color: Color = Theme.color.container.surface,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .stroke(color, shape)
            .padding(contentPadding),
        contentAlignment = Alignment.Center
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