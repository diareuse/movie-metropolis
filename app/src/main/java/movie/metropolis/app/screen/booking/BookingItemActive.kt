package movie.metropolis.app.screen.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.textPlaceholder

@Composable
fun BookingItemActive(
    name: String,
    cinema: String,
    date: String,
    time: String,
    poster: ImageView?,
    duration: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookingItemActiveLayout(
        modifier = modifier,
        posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
        cinema = { Text(cinema) },
        poster = { MoviePoster(url = poster?.url) },
        name = { Text(name) },
        time = { Text("%s @ %s".format(date, time)) },
        duration = { Text(duration) },
        onClick = onClick
    )
}

@Composable
fun BookingItemActive(
    modifier: Modifier = Modifier,
) {
    BookingItemActiveLayout(
        modifier = modifier,
        posterAspectRatio = DefaultPosterAspectRatio,
        cinema = {
            Text(
                "Foo Long cinema Boo",
                modifier = Modifier.textPlaceholder(
                    true,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        poster = { MoviePoster(url = null) },
        name = { Text("Long placeholder", modifier = Modifier.textPlaceholder(true)) },
        time = { Text("Mar 13 2022 @ 11:11", modifier = Modifier.textPlaceholder(true)) },
        duration = { Text("1h 30m", modifier = Modifier.textPlaceholder(true)) },
        onClick = null
    )
}

@Composable
private fun BookingItemActiveLayout(
    posterAspectRatio: Float,
    cinema: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    name: @Composable () -> Unit,
    time: @Composable () -> Unit,
    duration: @Composable () -> Unit,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                ) {
                    cinema()
                }
            }
            Surface(
                tonalElevation = 1.dp,
                shape = MaterialTheme.shapes.large
            ) {
                Box {
                    Image(
                        modifier = Modifier
                            .size(100.dp)
                            .scale(2f)
                            .alpha(.1f)
                            .align(Alignment.BottomEnd),
                        painter = painterResource(id = R.drawable.ic_movie),
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(LocalContentColor.current)
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onClick ?: {}, enabled = onClick != null)
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .shadow(24.dp)
                                .clip(MaterialTheme.shapes.medium)
                                .height(150.dp)
                                .aspectRatio(posterAspectRatio)
                                .background(MaterialTheme.colorScheme.surface),
                            contentAlignment = Alignment.Center,
                        ) {
                            poster()
                        }
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.titleLarge.copy(
                                    textAlign = TextAlign.Center
                                )
                            ) {
                                name()
                            }
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
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
        BookingItemActive(
            modifier = Modifier.padding(16.dp),
            name = "The Woman King",
            cinema = "Foo bar Cinemas, Long Long City",
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
private fun PreviewLoading() {
    Theme {
        BookingItemActive(modifier = Modifier.padding(16.dp))
    }
}