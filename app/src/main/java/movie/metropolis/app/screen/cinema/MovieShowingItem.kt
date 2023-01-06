package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
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