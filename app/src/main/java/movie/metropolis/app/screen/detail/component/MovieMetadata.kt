package movie.metropolis.app.screen.detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.mapNotNull
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.AppImage
import movie.style.imagePlaceholder
import movie.style.layout.CutoutLayout
import movie.style.modifier.surface
import movie.style.theme.Theme
import movie.style.theme.contentColorFor

@Composable
fun MovieMetadata(
    detail: Loadable<MovieDetailView>,
    poster: Loadable<ImageView>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        val image = poster.getOrNull()
        val color = image?.spotColor ?: Color.Black
        val rating = detail.mapNotNull { it.rating }.getOrNull()
        CutoutLayout(
            modifier = Modifier
                .fillMaxWidth(.3f)
                .aspectRatio(poster.getOrNull()?.aspectRatio ?: DefaultPosterAspectRatio),
            color = color,
            shape = Theme.container.poster,
            overlay = {
                if (rating != null) Text(
                    modifier = Modifier
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    text = rating,
                    color = Theme.color.contentColorFor(color),
                    style = Theme.textStyle.body
                )
            }
        ) {
            AppImage(
                modifier = Modifier
                    .fillMaxSize()
                    .imagePlaceholder(image == null)
                    .surface(Theme.color.container.background),
                url = image?.url
            )
        }
        MovieDetailColumn(detail = detail)
    }
}