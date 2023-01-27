package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import movie.style.AppImage
import movie.style.haptic.withHaptics
import movie.style.theme.Theme

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
    onLongPress: ((Boolean) -> Unit)? = null
) {
    if (rating == null) MoviePoster(url, modifier, onClick, onLongPress)
    else Box(modifier = modifier) {
        MoviePoster(url, onClick = onClick, onLongPress = onLongPress)
        val shape = Theme.container.card.copy(
            topStart = ZeroCornerSize,
            bottomEnd = ZeroCornerSize,
            topEnd = ZeroCornerSize
        )
        Text(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(Theme.color.container.tertiary, shape)
                .padding(12.dp)
                .padding(start = 4.dp),
            text = rating,
            style = Theme.textStyle.title
        )
    }
}