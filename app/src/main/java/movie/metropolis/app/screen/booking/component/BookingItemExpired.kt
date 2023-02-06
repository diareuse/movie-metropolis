package movie.metropolis.app.screen.booking.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movie.metropolis.app.R
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.metropolis.app.screen.listing.component.MoviePoster
import movie.metropolis.app.screen.listing.component.MovieSubText
import movie.metropolis.app.screen.listing.component.MovieTitleText
import movie.style.layout.EmptyShapeLayout
import movie.style.layout.PreviewLayout
import movie.style.layout.RibbonOverlayLayout
import movie.style.theme.Theme

@Composable
fun BookingItemExpired(
    modifier: Modifier = Modifier,
) {
    BookingItemExpiredLayout(
        modifier = modifier,
        poster = {
            MoviePoster(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(DefaultPosterAspectRatio),
                url = null,
                rating = null
            )
        },
        title = {
            MovieTitleText(text = "#".repeat(10), maxLines = 1, isLoading = true)
        },
        date = {
            MovieSubText(text = "#".repeat(16), isLoading = true)
        },
        time = {
            MovieSubText(text = "#".repeat(5), isLoading = true)
        }
    )
}

@Composable
fun BookingItemExpired(
    poster: ImageView?,
    date: String,
    time: String,
    name: String,
    rating: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BookingItemExpiredLayout(
        modifier = modifier,
        poster = {
            MoviePoster(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(poster?.aspectRatio ?: DefaultPosterAspectRatio),
                url = poster?.url,
                rating = rating,
                shadowColor = poster?.spotColor ?: Color.Black,
                onClick = onClick,
                contentFrame = {
                    RibbonOverlayLayout(
                        ribbon = {
                            Text(
                                text = stringResource(id = R.string.expired),
                                modifier = Modifier.padding(4.dp)
                            )
                        },
                        content = it
                    )
                }
            )
        },
        title = {
            AnimatedVisibility(visible = poster == null) {
                MovieTitleText(text = name, maxLines = 1)
            }
        },
        date = {
            MovieSubText(text = date)
        },
        time = {
            MovieSubText(text = time)
        }
    )
}

@Composable
fun BookingItemExpiredEmpty(
    modifier: Modifier = Modifier,
) {
    BookingItemExpiredLayout(
        modifier = modifier,
        poster = {
            EmptyShapeLayout(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(DefaultPosterAspectRatio),
                color = Theme.color.container.error,
                contentPadding = PaddingValues(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        modifier = Modifier.size(48.dp),
                        painter = painterResource(id = R.drawable.ic_hourglass),
                        contentDescription = null,
                        tint = Theme.color.container.error
                    )
                    Text(
                        stringResource(R.string.empty_booking_expired),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    )
}

@Composable
fun BookingItemExpiredFailure(
    modifier: Modifier = Modifier,
) {
    BookingItemExpiredLayout(
        modifier = modifier,
        poster = {
            EmptyShapeLayout(
                modifier = Modifier
                    .fillMaxHeight()
                    .aspectRatio(DefaultPosterAspectRatio),
                color = Theme.color.container.error,
                contentPadding = PaddingValues(24.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("🥺", style = Theme.textStyle.title.copy(fontSize = 48.sp))
                    Text(stringResource(R.string.error_booking), textAlign = TextAlign.Center)
                }
            }
        }
    )

}

@Composable
private fun BookingItemExpiredLayout(
    poster: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    title: (@Composable () -> Unit)? = null,
    date: (@Composable () -> Unit)? = null,
    time: (@Composable () -> Unit)? = null,
    posterHeight: Dp = 200.dp
) {
    Column(
        modifier = modifier.width(IntrinsicSize.Min),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.height(posterHeight)) {
            poster()
        }
        if (title != null || date != null || time != null) {
            Spacer(Modifier.height(8.dp))
            Column(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                title?.invoke()
                date?.invoke()
                time?.invoke()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() = PreviewLayout {
    val image = object : ImageView {
        override val aspectRatio = DefaultPosterAspectRatio
        override val url = "https://example.org"
        override val spotColor = Color.Black
    }
    BookingItemExpired(
        poster = image,
        date = "13 Dec 2022",
        time = "12:21",
        name = "Epic Goozeboo",
        rating = "82%",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNoPoster() = PreviewLayout {
    BookingItemExpired(
        poster = null,
        date = "13 Dec 2022",
        time = "12:21",
        name = "Epic Goozeboo",
        rating = "82%",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewNoPosterNoRating() = PreviewLayout {
    BookingItemExpired(
        poster = null,
        date = "13 Dec 2022",
        time = "12:21",
        name = "Epic Goozeboo",
        rating = null,
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun Preview2() = PreviewLayout {
    BookingItemExpired()
}

@Preview(showBackground = true)
@Composable
private fun Preview3() = PreviewLayout {
    BookingItemExpiredFailure()
}

@Preview(showBackground = true)
@Composable
private fun Preview4() = PreviewLayout {
    BookingItemExpiredEmpty()
}