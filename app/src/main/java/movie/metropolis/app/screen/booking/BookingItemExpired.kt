package movie.metropolis.app.screen.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.detail.ImageViewPreview
import movie.metropolis.app.screen.listing.DefaultPosterAspectRatio
import movie.metropolis.app.screen.listing.MoviePoster
import movie.style.haptic.withHaptics
import movie.style.layout.EmptyStackedCardLayout
import movie.style.layout.PosterLayout
import movie.style.layout.StackedCardLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme

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
        name = { Text("#".repeat(14), Modifier.textPlaceholder(true)) },
        time = { Text("#".repeat(19), Modifier.textPlaceholder(true)) },
        duration = { Text("#".repeat(6), Modifier.textPlaceholder(true)) }
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
    StackedCardLayout(
        modifier = modifier,
        color = Theme.color.container.error,
        contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
        headline = { Text(stringResource(R.string.expired)) },
        background = {
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
        }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = onClick?.withHaptics() ?: {},
                    enabled = onClick != null
                )
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

@Composable
fun BookingItemExpiredEmpty(
    modifier: Modifier = Modifier,
) {
    EmptyStackedCardLayout(
        modifier = modifier,
        color = Theme.color.container.error,
        contentPadding = PaddingValues(24.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Icon(
                painterResource(id = R.drawable.ic_hourglass),
                null,
                tint = Theme.color.container.error
            )
            Text(stringResource(R.string.empty_booking_expired))
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

@Preview(showBackground = true)
@Composable
private fun PreviewEmpty() {
    Theme {
        BookingItemExpiredEmpty(modifier = Modifier.padding(16.dp))
    }
}