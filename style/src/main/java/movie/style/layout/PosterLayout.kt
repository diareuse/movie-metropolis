package movie.style.layout

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme

@Composable
fun PosterLayout(
    posterAspectRatio: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.aspectRatio(posterAspectRatio),
        shape = Theme.container.poster,
        color = Theme.color.container.background,
        shadowElevation = 24.dp
    ) {
        content()
    }
}

@Composable
fun PosterLayout(
    posterAspectRatio: Float,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .aspectRatio(posterAspectRatio)
            .shadow(
                elevation = 24.dp,
                shape = Theme.container.poster,
                ambientColor = shadowColor,
                spotColor = shadowColor
            ),
        shape = Theme.container.poster,
        color = Theme.color.container.background
    ) {
        content()
    }
}