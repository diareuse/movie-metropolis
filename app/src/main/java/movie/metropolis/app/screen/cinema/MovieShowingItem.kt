package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.detail.ShowingItemSection
import movie.metropolis.app.screen.detail.ShowingItemTime
import movie.metropolis.app.screen.detail.ShowingLayout

@Composable
fun MovieShowingItem(
    movie: MovieBookingView.Movie,
    availability: Map<MovieBookingView.LanguageAndType, List<MovieBookingView.Availability>>,
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
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(.2f)
                    .blur(4.dp),
                model = movie.poster,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
    ) {
        ShowingItemTime(
            modifier = Modifier.clickable { onClick(it.url) },
            time = it.startsAt
        )
    }
}