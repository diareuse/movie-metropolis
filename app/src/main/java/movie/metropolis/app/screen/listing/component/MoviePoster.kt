package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import movie.style.AppImage
import movie.style.haptic.withHaptics

@Composable
fun MoviePoster(
    url: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongPress: ((Boolean) -> Unit)? = null
) {
    AppImage(
        modifier = modifier
            .fillMaxSize()
            .let { if (onClick == null) it else it.clickable(onClick = onClick.withHaptics()) }
            .let { if (onLongPress == null) it else it.detectLongPress(onLongPress) },
        url = url
    )
}