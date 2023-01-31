package movie.metropolis.app.screen.listing.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.detail.component.FavoriteButton
import movie.style.layout.CutoutShape
import movie.style.layout.PosterLayout
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import movie.style.theme.extendBy

@Composable
fun MovieItem(
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
fun MovieItem(
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
        if (rating == null) Theme.container.poster.extendBy(8.dp)
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

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    height: Dp = 225.dp
) {
    MovieItemLayout(
        modifier = modifier,
        height = height,
        shadowColor = Color.Black,
        poster = { MoviePoster(url = null, modifier = Modifier.fillMaxSize()) {} }
    )
}

@Composable
fun MovieItemEmpty(modifier: Modifier = Modifier) {
    MovieItemLayout(
        modifier = modifier,
        poster = {
            Surface(
                shape = Theme.container.poster
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🧐", style = Theme.textStyle.title.copy(fontSize = 48.sp))
                }
            }
        },
        text = {
            MovieSubText(text = stringResource(R.string.empty_movie_sub))
            MovieTitleText(text = stringResource(R.string.empty_movie))
        },
        shadowColor = Color.Black
    )
}

@Composable
fun MovieItemError(modifier: Modifier = Modifier) {
    MovieItemLayout(
        modifier = modifier,
        poster = {
            Surface(
                shape = Theme.container.poster
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("😦", style = Theme.textStyle.title.copy(fontSize = 48.sp))
                }
            }
        },
        text = {
            MovieSubText(text = stringResource(R.string.error_movie_sub))
            MovieTitleText(text = stringResource(R.string.error_movie))
        },
        shadowColor = Color.Black
    )
}

@Composable
fun MovieItemLayout(
    poster: @Composable () -> Unit,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    text: (@Composable ColumnScope.() -> Unit)? = null,
    posterOverlay: @Composable BoxScope.() -> Unit = {},
    posterAspectRatio: Float = DefaultPosterAspectRatio,
    height: Dp = 225.dp,
    shape: Shape = Theme.container.poster,
    textPadding: PaddingValues = PaddingValues(
        top = 16.dp,
        start = 12.dp,
        end = 12.dp,
        bottom = 0.dp
    )
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.height(height),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(posterAspectRatio)
            ) {
                PosterLayout(
                    modifier = Modifier.fillMaxSize(),
                    posterAspectRatio = posterAspectRatio,
                    shadowColor = animateColorAsState(shadowColor).value,
                    shape = shape,
                    content = poster
                )
                posterOverlay()
            }
        }
        if (text != null) Column(
            modifier = Modifier
                .padding(textPadding)
                .fillMaxWidth(),
            content = text
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmpty() {
    Theme {
        MovieItemEmpty(modifier = Modifier.padding(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewError() {
    Theme {
        MovieItemError(modifier = Modifier.padding(16.dp))
    }
}

const val DefaultPosterAspectRatio = 0.67375886f