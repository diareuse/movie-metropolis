package movie.metropolis.app.screen.profile.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import kotlinx.coroutines.delay
import movie.style.Image
import movie.style.rememberImageState
import kotlin.math.max

@Composable
fun AnimatedImageBackground(
    urls: List<String>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Box(modifier = modifier) {
    var index by remember { mutableIntStateOf(0) }
    AnimatedContent(
        modifier = Modifier.matchParentSize(),
        targetState = index,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        val state = rememberImageState(url = urls.getOrNull(it) ?: return@AnimatedContent)
        Image(state)
    }
    LaunchedEffect(index) {
        delay(5000)
        index = (index + 1) % max(urls.size, 1)
    }
    content()
}