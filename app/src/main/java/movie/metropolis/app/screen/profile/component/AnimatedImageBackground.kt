package movie.metropolis.app.screen.profile.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import movie.style.AppImage
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
        val surface = MaterialTheme.colorScheme.surface
        AppImage(
            modifier = Modifier
                .fillMaxSize()
                .alpha(.2f)
                .drawWithContent {
                    val brush = Brush.verticalGradient(
                        listOf(Color.Transparent, surface)
                    )
                    drawContent()
                    drawRect(brush)
                }
                .blur(16.dp),
            url = urls.getOrNull(it) ?: return@AnimatedContent
        )
    }
    LaunchedEffect(index) {
        delay(5000)
        index = (index + 1) % max(urls.size, 1)
    }
    content()
}