package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.detail.component.FavoriteButton
import movie.style.layout.CutoutLayout
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

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
        poster = {
            MoviePoster(
                url = poster?.url,
                onClick = onClick,
                onLongPress = onLongPress
            )
        },
        shadowColor = poster?.spotColor ?: Color.Black,
        modifier = modifier,
        text = {
            MovieSubText(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = subtext
            )
        },
        posterOverlay = {
            val spotColor = poster?.spotColor ?: Theme.color.container.background
            val color = Theme.color.contentColorFor(spotColor)
            FavoriteButton(
                isChecked = isFavorite,
                onClick = onClickFavorite,
                tintChecked = color,
                tint = color
            )
        },
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        height = height,
        textPadding = PaddingValues(top = 8.dp)
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
    MovieItemLayout(
        poster = {
            MoviePoster(
                url = poster?.url,
                onClick = onClick,
                onLongPress = onLongPress
            )
        },
        shadowColor = poster?.spotColor ?: Color.Black,
        modifier = modifier,
        posterOverlay = {
            val spotColor = poster?.spotColor ?: Theme.color.container.background
            val color = Theme.color.contentColorFor(spotColor)
            if (rating != null) Text(
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                text = rating,
                color = color,
                style = Theme.textStyle.emphasis
            )
        },
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        height = height
    )
}

@Composable
fun MovieItem(
    modifier: Modifier = Modifier,
    height: Dp = 225.dp
) {
    MovieItemLayout(
        poster = { MoviePoster(url = null, modifier = Modifier.fillMaxSize()) {} },
        shadowColor = Color.Black,
        modifier = modifier,
        height = height
    )
}

@Composable
fun MovieItemEmpty(modifier: Modifier = Modifier) {
    MovieItemLayout(
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
        shadowColor = Color.Black,
        modifier = modifier,
        text = {
            MovieSubText(text = stringResource(R.string.empty_movie_sub))
            MovieTitleText(text = stringResource(R.string.empty_movie))
        }
    )
}

@Composable
fun MovieItemError(modifier: Modifier = Modifier) {
    MovieItemLayout(
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
        shadowColor = Color.Black,
        modifier = modifier,
        text = {
            MovieSubText(text = stringResource(R.string.error_movie_sub))
            MovieTitleText(text = stringResource(R.string.error_movie))
        }
    )
}

@Composable
fun MovieItemLayout(
    poster: @Composable () -> Unit,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    text: @Composable (ColumnScope.() -> Unit)? = null,
    posterOverlay: @Composable () -> Unit = {},
    posterAspectRatio: Float = DefaultPosterAspectRatio,
    height: Dp = 225.dp,
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
            CutoutLayout(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(posterAspectRatio),
                color = shadowColor,
                shape = Theme.container.poster,
                overlay = posterOverlay,
                content = poster
            )
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