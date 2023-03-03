package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.map
import movie.metropolis.app.presentation.mapNotNull
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.detail.ImageViewPreview
import movie.metropolis.app.screen.detail.MovieDetailViewProvider
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.AppImage
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.textPlaceholder

@Composable
fun MovieMetadata(
    detail: Loadable<MovieDetailView>,
    poster: Loadable<ImageView>,
    modifier: Modifier = Modifier,
) {
    MovieMetadataLayout(
        modifier = modifier,
        aspectRatio = poster.getOrNull()?.aspectRatio ?: DefaultPosterAspectRatio,
        color = poster.getOrNull()?.spotColor ?: Color.Black,
        rating = {
            detail
                .mapNotNull { it.rating }
                .onLoading {
                    Text(
                        "#".repeat(3),
                        Modifier
                            .padding(12.dp, 8.dp)
                            .textPlaceholder()
                    )
                }
                .onSuccess { Text(it, Modifier.padding(12.dp, 8.dp)) }
        },
        image = {
            poster
                .map { it.url }
                .onLoading {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .imagePlaceholder()
                    )
                }
                .onSuccess { AppImage(it, Modifier.fillMaxSize()) }
                .onFailure { AppImage(null, Modifier.fillMaxSize()) }
        }
    ) {
        MovieDetailColumn(detail = detail)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MovieMetadataPreview(
    @PreviewParameter(MovieMetadataParameter::class)
    parameter: MovieMetadataParameter.Data
) = PreviewLayout {
    MovieMetadata(parameter.detail, parameter.poster)
}

class MovieMetadataParameter : CollectionPreviewParameterProvider<MovieMetadataParameter.Data>(
    listOf(
        Data(Loadable.loading(), Loadable.loading()),
        Data(detail = Loadable.loading()),
        Data(poster = Loadable.loading()),
        Data(Loadable.failure(Throwable()), Loadable.failure(Throwable())),
        Data(detail = Loadable.failure(Throwable())),
        Data(poster = Loadable.failure(Throwable())),
    )
) {
    data class Data(
        val detail: Loadable<MovieDetailView> = Loadable.success(MovieDetailViewProvider().values.first()),
        val poster: Loadable<ImageView> = Loadable.success(ImageViewPreview())
    )
}