package movie.style.layout

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme
import movie.style.theme.extendBy

@Composable
fun EmptyStackedCardLayout(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    color: Color = Theme.color.container.surface,
    content: @Composable () -> Unit
) {
    EmptyShapeLayout(
        modifier = modifier,
        color = color,
        contentPadding = PaddingValues(start = 8.dp, top = 32.dp, end = 8.dp, bottom = 8.dp),
        shape = Theme.container.card.extendBy(padding = 8.dp)
    ) {
        EmptyShapeLayout(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = contentPadding,
            color = color,
            shape = Theme.container.card,
            content = content
        )
    }
}