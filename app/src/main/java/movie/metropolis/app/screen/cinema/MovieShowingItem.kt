package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import movie.metropolis.app.feature.image.imageRequestOf
import movie.metropolis.app.model.AvailabilityView
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.detail.ShowingItemSection
import movie.metropolis.app.screen.detail.ShowingItemTime
import movie.metropolis.app.screen.detail.ShowingLayout
import movie.metropolis.app.view.textPlaceholder

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
            var size by remember { mutableStateOf(IntSize.Zero) }
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.2f)
                    .blur(4.dp)
                    .onGloballyPositioned { size = it.size },
                model = imageRequestOf(movie.poster, size),
                contentDescription = null,
                contentScale = ContentScale.Crop
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
            "a" to List(3) { it },
            "b" to List(1) { it },
            "c" to List(2) { it },
        ),
        key = { it },
        title = { Text("My super awesome cinema", Modifier.textPlaceholder(true)) },
        section = { ShowingItemSection(type = "type", language = "English", isLoading = true) }
    ) {
        ShowingItemTime(
            modifier = Modifier.textPlaceholder(true),
            time = "10.00"
        )
    }
}