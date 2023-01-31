package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.detail.component.FavoriteButton
import movie.style.layout.CutoutShape
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import movie.style.theme.extendBy

@Composable
fun MovieItemAlt(
    subtext: String,
    poster: ImageView?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onClickFavorite: () -> Unit,
    onLongPress: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 225.dp
) {
    MovieItemLayout(
        modifier = modifier,
        height = height,
        shadowColor = poster?.spotColor ?: Color.Black,
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        shape = CutoutShape.from(Theme.container.poster.extendBy(8.dp), 56.dp),
        poster = {
            MoviePoster(
                url = poster?.url,
                onClick = onClick,
                onLongPress = onLongPress
            )
        },
        posterOverlay = {
            val spotColor = poster?.spotColor ?: Theme.color.container.background
            val color = Theme.color.contentColorFor(spotColor)
            FavoriteButton(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .shadow(
                        16.dp,
                        shape = Theme.container.button,
                        clip = false,
                        spotColor = spotColor,
                        ambientColor = spotColor
                    )
                    .background(spotColor, Theme.container.button)
                    .clip(Theme.container.button),
                isChecked = isFavorite,
                onClick = onClickFavorite,
                tintChecked = color,
                tint = color
            )
        },
        textPadding = PaddingValues(top = 8.dp),
        text = {
            MovieSubText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = subtext
            )
        }
    )
}

@Composable
fun MovieItemAlt(
    rating: String?,
    poster: ImageView?,
    onClick: () -> Unit,
    onLongPress: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 225.dp
) {
    var ratingWidth by remember { mutableStateOf(0.dp) }
    var ratingHeight by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current
    MovieItemLayout(
        modifier = modifier,
        height = height,
        shadowColor = poster?.spotColor ?: Color.Black,
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        shape =
        if (rating == null) Theme.container.poster
        else CutoutShape.from(
            shape = Theme.container.poster.extendBy(8.dp),
            width = ratingWidth + 8.dp,
            height = ratingHeight + 8.dp
        ),
        poster = {
            MoviePoster(
                url = poster?.url,
                onClick = onClick,
                onLongPress = onLongPress
            )
        },
        posterOverlay = {
            val spotColor = poster?.spotColor ?: Theme.color.container.background
            val color = Theme.color.contentColorFor(spotColor)
            if (rating != null) Text(
                modifier = Modifier
                    .onGloballyPositioned {
                        with(density) {
                            ratingWidth = it.size.width.toDp()
                            ratingHeight = it.size.height.toDp()
                        }
                    }
                    .align(Alignment.TopEnd)
                    .shadow(
                        16.dp,
                        shape = Theme.container.button,
                        clip = false,
                        spotColor = spotColor,
                        ambientColor = spotColor
                    )
                    .background(spotColor, Theme.container.button)
                    .clip(Theme.container.button)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = rating,
                color = color,
                style = Theme.textStyle.emphasis
            )
        }
    )
}