package movie.metropolis.app.screen.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.detail.ImageViewPreview
import movie.metropolis.app.screen.listing.DefaultPosterAspectRatio
import movie.metropolis.app.screen.listing.MoviePoster
import movie.style.haptic.withHaptics
import movie.style.layout.PosterLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme
import movie.style.theme.extendBy

@Composable
fun BookingItemExpired(
    name: String,
    date: String,
    time: String,
    poster: ImageView?,
    duration: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookingItemExpiredLayout(
        modifier = modifier,
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        poster = { MoviePoster(url = poster?.url) },
        name = { Text(name) },
        time = { Text("%s @ %s".format(date, time)) },
        duration = { Text(duration) },
        onClick = onClick
    )
}

@Composable
fun BookingItemExpired(
    modifier: Modifier = Modifier,
) {
    BookingItemExpiredLayout(
        modifier = modifier,
        posterAspectRatio = DefaultPosterAspectRatio,
        poster = { MoviePoster(url = null) },
        name = { Text("Very long name", Modifier.textPlaceholder(true)) },
        time = { Text("Mar 13 2022 @ 11:11", Modifier.textPlaceholder(true)) },
        duration = { Text("1h 30m", Modifier.textPlaceholder(true)) }
    )
}

@Composable
private fun BookingItemExpiredLayout(
    posterAspectRatio: Float,
    poster: @Composable () -> Unit,
    name: @Composable () -> Unit,
    time: @Composable () -> Unit,
    duration: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Surface(
        modifier = modifier,
        color = Theme.color.container.error,
        shape = Theme.container.card.extendBy(padding = 8.dp),
        contentColor = Theme.color.content.error
    ) {
        Column(
            Modifier
                .padding(horizontal = 8.dp)
                .padding(bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Expired", Modifier.padding(8.dp), fontWeight = FontWeight.Bold)
            Surface(
                tonalElevation = 1.dp,
                shape = Theme.container.card
            ) {
                Box(
                    modifier = Modifier
                        .clickable(
                            onClick = onClick?.withHaptics() ?: {},
                            enabled = onClick != null
                        )
                ) {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .scale(1.5f)
                            .alpha(.1f)
                            .align(Alignment.BottomEnd),
                        painter = painterResource(id = R.drawable.ic_movie),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalContentColor.current)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PosterLayout(
                            modifier = Modifier.height(100.dp),
                            posterAspectRatio = posterAspectRatio,
                            content = poster
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides Theme.textStyle.title.copy(
                                    textAlign = TextAlign.Center
                                )
                            ) {
                                name()
                            }
                            CompositionLocalProvider(
                                LocalTextStyle provides Theme.textStyle.body.copy(
                                    textAlign = TextAlign.Center
                                )
                            ) {
                                time()
                                duration()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        BookingItemExpired(
            modifier = Modifier.padding(16.dp),
            name = "The Woman King",
            date = "Mar 13. 2022",
            time = "10:30",
            poster = ImageViewPreview(
                url = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
                aspectRatio = DefaultPosterAspectRatio
            ),
            duration = "1h 30m",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BookingItemExpired() {
    Theme {
        BookingItemExpired(modifier = Modifier.padding(16.dp))
    }
}