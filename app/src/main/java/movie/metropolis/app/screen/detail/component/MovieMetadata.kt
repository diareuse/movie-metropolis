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
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.detail.ImageViewPreview
import movie.metropolis.app.screen.detail.MovieDetailViewProvider
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.style.imagePlaceholder
import movie.style.layout.PreviewLayout
import movie.style.rememberPaletteImageState
import movie.style.textPlaceholder

@Composable
fun MovieMetadata(
    detail: Loadable<MovieDetailView>,
    modifier: Modifier = Modifier,
) {
    detail.onLoading {
        MovieMetadataLoading(modifier = modifier)
    }.onSuccess {
        MovieMetadataLoaded(modifier = modifier, detail = it)
    }.onFailure {
        MovieMetadataError(modifier = modifier)
    }
}

@Composable
private fun MovieMetadataLoaded(
    detail: MovieDetailView,
    modifier: Modifier = Modifier,
) {
    val state = rememberPaletteImageState(url = detail.poster?.url.orEmpty())
    MovieMetadataLayout(
        modifier = modifier,
        aspectRatio = detail.poster?.aspectRatio ?: DefaultPosterAspectRatio,
        color = state.palette.color,
        rating = {
            val r = detail.rating
            if (r != null) Text(
                modifier = Modifier.padding(12.dp, 8.dp),
                text = r
            )
        },
        image = {
            Box(
                Modifier
                    .fillMaxSize()
                    .imagePlaceholder()
            )
        }
    ) {
        MovieDetailLayout(
            title = { Text(detail.name) },
            details = {
                Text(
                    "%s • %s • %s".format(
                        detail.duration,
                        detail.countryOfOrigin,
                        detail.releasedAt
                    )
                )
            },
            directors = { detail.directors.joinToString() },
            cast = { detail.cast.joinToString() }
        )
    }
}

@Composable
private fun MovieMetadataError(
    modifier: Modifier = Modifier
) {
    MovieMetadataLayout(
        modifier = modifier,
        aspectRatio = DefaultPosterAspectRatio,
        color = Color.Black,
        rating = {},
        image = { Box(Modifier) }
    ) {
    }
}

@Composable
private fun MovieMetadataLoading(
    modifier: Modifier = Modifier
) {
    MovieMetadataLayout(
        modifier = modifier,
        aspectRatio = DefaultPosterAspectRatio,
        color = Color.Black,
        rating = {
            Text(
                modifier = Modifier
                    .padding(12.dp, 8.dp)
                    .textPlaceholder(),
                text = "#".repeat(3)
            )
        },
        image = {
            Box(
                Modifier
                    .fillMaxSize()
                    .imagePlaceholder()
            )
        }
    ) {
        MovieDetailLayout(
            title = { Text("#".repeat(10), Modifier.textPlaceholder()) },
            details = { Text("#".repeat(22), Modifier.textPlaceholder()) },
            directors = { Text("#".repeat(13), Modifier.textPlaceholder()) },
            cast = { Text("#".repeat(28), Modifier.textPlaceholder()) }
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MovieMetadataPreview(
    @PreviewParameter(MovieMetadataParameter::class)
    parameter: MovieMetadataParameter.Data
) = PreviewLayout {
    MovieMetadata(parameter.detail)
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