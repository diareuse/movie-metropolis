package movie.metropolis.app.screen.listing

import androidx.compose.foundation.background
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.screen.detail.FavoriteButton
import movie.style.AppImage
import movie.style.haptic.withHaptics
import movie.style.layout.PosterLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme

@Composable
fun MovieItem(
    name: String,
    subtext: String,
    poster: ImageView?,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onClickFavorite: () -> Unit,
    modifier: Modifier = Modifier,
    height: Dp = 225.dp
) {
    MovieItemLayout(
        modifier = modifier,
        height = height,
        shadowColor = poster?.spotColor ?: Color.Black,
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        poster = {
            MoviePoster(url = poster?.url, onClick = onClick)
            FavoriteButton(
                modifier = Modifier.align(Alignment.TopEnd),
                isChecked = isFavorite,
                onClick = onClickFavorite
            )
        },
        text = {
            MovieSubText(text = subtext)
            MovieTitleText(text = name)
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
        poster = { MoviePoster(url = null, modifier = Modifier.fillMaxSize(), onClick = {}) },
        text = {
            MovieSubText(text = "#".repeat(4), isLoading = true)
            Spacer(Modifier.size(4.dp))
            MovieTitleText(text = "#".repeat(9), isLoading = true)
        }
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
    poster: @Composable BoxScope.() -> Unit,
    text: @Composable ColumnScope.() -> Unit,
    shadowColor: Color,
    modifier: Modifier = Modifier,
    posterAspectRatio: Float = DefaultPosterAspectRatio,
    height: Dp = 225.dp
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.height(height),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PosterLayout(
                modifier = Modifier.fillMaxHeight(),
                posterAspectRatio = posterAspectRatio,
                shadowColor = animateColorAsState(shadowColor).value
            ) {
                Box(modifier = Modifier.fillMaxSize(), content = poster)
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 16.dp, start = 12.dp, end = 12.dp)
                .fillMaxWidth(),
            content = text
        )
    }
}

@Composable
fun MoviePoster(
    url: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    AppImage(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                enabled = url != null && onClick != null,
                onClick = onClick?.withHaptics() ?: {}),
        url = url
    )
}

@Composable
fun MoviePoster(
    url: String?,
    rating: String?,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    if (rating == null) MoviePoster(url, modifier, onClick)
    else Box(modifier = modifier) {
        MoviePoster(url, onClick = onClick)
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

@Composable
fun MovieSubText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Text(
        modifier = modifier.textPlaceholder(isLoading),
        text = text,
        style = Theme.textStyle.caption
    )
}

@Composable
fun MovieTitleText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Text(
        modifier = modifier.textPlaceholder(visible = isLoading),
        text = text,
        style = Theme.textStyle.body,
        fontWeight = FontWeight.Bold,
        maxLines = 2,
        minLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MovieItem(
            modifier = Modifier.padding(16.dp),
            name = "Black Adam",
            subtext = "12. 11. 2022",
            isFavorite = true,
            poster = ImageView(
                DefaultPosterAspectRatio,
                "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5145S2R-lg.jpg"
            ),
            onClickFavorite = {},
            onClick = {}
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

private fun VideoView(url: String) = object : VideoView {
    override val url: String
        get() = url

}

private fun ImageView(
    aspectRatio: Float,
    url: String
) = object : ImageView {
    override val aspectRatio: Float
        get() = aspectRatio
    override val url: String
        get() = url
    override val spotColor: Color
        get() = Color.Yellow
}