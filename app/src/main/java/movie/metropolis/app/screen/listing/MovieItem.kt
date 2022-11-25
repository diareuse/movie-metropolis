package movie.metropolis.app.screen.listing

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.ShapeToken
import movie.metropolis.app.view.placeholder

@Composable
fun MovieItem(
    name: String,
    subtext: String,
    video: VideoView?,
    poster: ImageView?,
    onClickVideo: (String) -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    MovieItemLayout(
        modifier = modifier,
        posterAspectRatio = poster?.aspectRatio ?: (4f / 3),
        poster = {
            MoviePoster(url = poster?.url, onClick = onClick)
            if (video != null) Image(
                modifier = Modifier
                    .size(48.dp)
                    .clickable(role = Role.Button) { onClickVideo(video.url) },
                painter = painterResource(id = R.drawable.ic_play),
                contentDescription = null,
                colorFilter = ColorFilter.tint(Color.White)
            )
        },
        text = {
            MovieSubText(text = subtext)
            MovieTitleText(text = name)
        }
    )
}

@Composable
fun MovieItem() {
    MovieItemLayout(
        poster = { MoviePoster(url = null, modifier = Modifier.fillMaxSize(), onClick = {}) },
        text = {
            MovieSubText(text = "2021", isLoading = true)
            Spacer(Modifier.size(4.dp))
            MovieTitleText(text = "Hi you :)", isLoading = true)
        }
    )
}

@Composable
fun MovieItemLayout(
    poster: @Composable BoxScope.() -> Unit,
    text: @Composable ColumnScope.() -> Unit,
    modifier: Modifier = Modifier,
    posterAspectRatio: Float = 3f / 5,
) {
    Column(modifier = modifier.width(IntrinsicSize.Min)) {
        Row(
            modifier = Modifier.height(225.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .shadow(24.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .fillMaxHeight()
                    .aspectRatio(posterAspectRatio)
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center,
                content = poster
            )
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
    AsyncImage(
        modifier = modifier
            .fillMaxSize()
            .placeholder(url == null, ShapeToken.Medium)
            .clickable(enabled = url != null && onClick != null, onClick = { onClick?.invoke() }),
        model = url ?: "",
        contentDescription = "",
        contentScale = ContentScale.Crop
    )
}

@Composable
fun MovieSubText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Text(
        modifier = modifier.placeholder(isLoading),
        text = text,
        style = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun MovieTitleText(
    text: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false
) {
    Text(
        modifier = modifier.placeholder(visible = isLoading),
        text = text,
        style = MaterialTheme.typography.bodyMedium,
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
            video = VideoView("https://www.youtube.com/watch?v=X0tOpBuYasI"),
            poster = ImageView(
                0.6741573f,
                "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5145S2R-lg.jpg"
            ),
            onClickVideo = {},
            onClick = {}
        )
    }
}

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
}