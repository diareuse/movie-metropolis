package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import movie.style.AppImage
import movie.style.haptic.withHaptics
import movie.style.layout.CutoutLayout
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

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

@Composable
fun MoviePoster(
    url: String?,
    rating: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    onLongPress: ((Boolean) -> Unit)? = null,
    shadowColor: Color = Color.Black,
    contentFrame: @Composable (content: @Composable () -> Unit) -> Unit = { it() }
) {
    CutoutLayout(
        modifier = modifier,
        color = shadowColor,
        shape = Theme.container.poster,
        overlay = {
            val color = Theme.color.contentColorFor(shadowColor)
            if (rating != null) Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = rating,
                color = color,
                style = Theme.textStyle.emphasis
            )
        },
        content = {
            contentFrame {
                MoviePoster(url = url, onClick = onClick, onLongPress = onLongPress)
            }
        }
    )
}