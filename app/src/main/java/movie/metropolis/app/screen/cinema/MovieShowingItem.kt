package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.detail.ShowingItemSection
import movie.metropolis.app.screen.detail.ShowingItemTime
import movie.metropolis.app.screen.detail.ShowingLayout
import movie.style.AppImage
import movie.style.textPlaceholder

@Composable
fun MovieShowingItem(
    movie: MovieBookingView.Movie,
    availability: Map<AvailabilityView.Type, List<AvailabilityView>>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShowingLayout(
        modifier = modifier,
        items = availability,
        key = { it.id },
        title = { Text(movie.name) },
        section = { ShowingItemSection(type = it.type, language = it.language) },
        background = {
            AppImage(
                modifier = Modifier
                    .fillMaxSize()
                    .parallax()
                    .alpha(.2f)
                    .blur(4.dp),
                url = movie.poster
            )
        }
    ) {
        ShowingItemTime(
            time = it.startsAt,
            onClick = { onClick(it.url) }
        )
    }
}

@Composable
fun MovieShowingItem(
    modifier: Modifier = Modifier
) {
    ShowingLayout(
        modifier = modifier,
        items = mapOf(
            "#" to List(3) { it },
            "##" to List(1) { it },
            "###" to List(2) { it },
        ),
        key = { it },
        title = { Text("#".repeat(23), Modifier.textPlaceholder(true)) },
        section = {
            ShowingItemSection(
                type = "#".repeat(4),
                language = "#".repeat(7),
                isLoading = true
            )
        }
    ) {
        ShowingItemTime(
            modifier = Modifier.textPlaceholder(true),
            time = "#".repeat(5)
        )
    }
}

fun Modifier.parallax(distance: Dp = 64.dp) = composed {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current
    val height = with(density) { configuration.screenHeightDp.dp.toPx() }
    val width = with(density) { configuration.screenWidthDp.dp.toPx() }
    val distancePx = with(density) { distance.roundToPx() }
    var center by remember { mutableStateOf(Offset.Zero) }
    val screen = Offset(width / 2, height / 2)
    val deltaCenter = center - screen
    val offset = Offset(
        distancePx * (deltaCenter.x.coerceIn(-screen.x, screen.x) / screen.x),
        distancePx * (deltaCenter.y.coerceIn(-screen.y, screen.y) / screen.y)
    )
    this
        .onGloballyPositioned {
            center = it.localToWindow((it.size / 2).toOffset())
        }
        .layout { measurable, constraints ->
            val placeable = measurable.measure(constraints + (distancePx * 2))
            layout(constraints.maxWidth, constraints.maxHeight) {
                placeable.place(offset.x.toInt() - distancePx, offset.y.toInt() - distancePx)
            }
        }
}

private fun IntSize.toOffset(): Offset {
    return Offset(width.toFloat(), height.toFloat())
}

private operator fun Constraints.plus(px: Int) = copy(
    minWidth = minWidth + px,
    maxWidth = maxWidth + px,
    minHeight = minHeight + px,
    maxHeight = maxHeight + px
)