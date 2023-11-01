package movie.style

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout

@Composable
fun BackgroundImage(
    state: ImageState,
    modifier: Modifier = Modifier,
) = AnimatedContent(
    modifier = modifier,
    targetState = state,
    transitionSpec = { fadeIn() togetherWith fadeOut() }
) {
    Image(
        modifier = Modifier
            .blur(16.dp)
            .alpha(.35f),
        state = it
    )
}

@Preview
@Composable
private fun BackgroundImagePreview() = PreviewLayout {
    BackgroundImage(rememberImageState(url = ""))
}