package movie.metropolis.app.screen.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FractionalThreshold
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.detail.ImageViewPreview
import movie.metropolis.app.screen.listing.DefaultPosterAspectRatio
import movie.metropolis.app.screen.listing.MoviePoster
import movie.style.AppIconButton
import movie.style.haptic.withHaptics
import movie.style.layout.PosterLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BookingItemActive(
    name: String,
    cinema: String,
    date: String,
    time: String,
    poster: ImageView?,
    duration: String,
    onClick: () -> Unit,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val state = rememberSwipeableState(initialValue = 0)
    var anchorSize by remember { mutableStateOf(0f) }
    val anchors = remember(anchorSize) { mapOf(0f to 0, -anchorSize to 1) }
    LaunchedEffect(state.currentValue) {
        delay(5000)
        if (state.currentValue != 0)
            state.animateTo(0)
    }
    Box(
        modifier = modifier.swipeable(
            state = state,
            anchors = anchors,
            thresholds = { _, _ -> FractionalThreshold(1f) },
            orientation = Orientation.Horizontal
        ),
        contentAlignment = Alignment.CenterEnd
    ) {
        AppIconButton(
            modifier = Modifier.onGloballyPositioned { anchorSize = it.size.width.toFloat() },
            painter = painterResource(id = R.drawable.ic_share),
            onClick = {
                scope.launch {
                    state.animateTo(0)
                }
                onShare()
            }
        )
        BookingItemActiveLayout(
            modifier = Modifier.offset {
                val offset = state.offset.value.takeUnless { it.isNaN() } ?: 0f
                IntOffset(offset.roundToInt(), 0)
            },
            posterAspectRatio = poster?.aspectRatio ?: DefaultPosterAspectRatio,
            cinema = { Text(cinema) },
            poster = { MoviePoster(url = poster?.url) },
            name = { Text(name) },
            time = { Text("%s @ %s".format(date, time)) },
            duration = { Text(duration) },
            onClick = onClick
        )
    }
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
                "#".repeat(20),
                modifier = Modifier.textPlaceholder(
                    true,
                    color = Theme.color.container.secondary
                )
            )
        },
        poster = { MoviePoster(url = null) },
        name = { Text("#".repeat(16), modifier = Modifier.textPlaceholder(true)) },
        time = { Text("#".repeat(19), modifier = Modifier.textPlaceholder(true)) },
        duration = { Text("#".repeat(6), modifier = Modifier.textPlaceholder(true)) },
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
        color = Theme.color.container.secondary,
        shape = Theme.container.card
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides Theme.textStyle.body.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                ) {
                    cinema()
                }
            }
            Surface(
                tonalElevation = 1.dp,
                shape = Theme.container.card
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
                            .clickable(
                                onClick = onClick?.withHaptics() ?: {},
                                enabled = onClick != null
                            )
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        PosterLayout(
                            modifier = Modifier.height(150.dp),
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
            onClick = {},
            onShare = {}
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