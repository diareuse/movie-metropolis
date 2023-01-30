package movie.style.layout

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme

@Composable
fun PosterLayout(
    posterAspectRatio: Float,
    modifier: Modifier = Modifier,
    shape: Shape = Theme.container.poster,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier.aspectRatio(posterAspectRatio),
        shape = shape,
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
    shape: Shape = Theme.container.poster,
    content: @Composable () -> Unit,
) {
    Surface(
        modifier = modifier
            .aspectRatio(posterAspectRatio)
            .shadow(
                elevation = 24.dp,
                shape = shape,
                ambientColor = shadowColor,
                spotColor = shadowColor,
                clip = false
            ),
        shape = shape,
        color = Theme.color.container.background
    ) {
        content()
    }
}