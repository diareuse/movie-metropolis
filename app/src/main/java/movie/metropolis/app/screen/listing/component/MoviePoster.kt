package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import movie.style.Image
import movie.style.rememberImageState

@Deprecated("Use Image directly")
@Composable
fun MoviePoster(
    url: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongPress: ((Boolean) -> Unit)? = null
) = when (url) {
    null -> Box(Modifier.fillMaxSize())
    else -> Image(
        modifier = modifier
            .fillMaxSize()
            .let { if (onClick == null) it else it.clickable(onClick = onClick) }
            .let { if (onLongPress == null) it else it.detectLongPress(onLongPress) },
        state = rememberImageState(url = url)
    )
}