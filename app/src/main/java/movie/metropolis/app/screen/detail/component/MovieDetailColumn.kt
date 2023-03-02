package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import movie.metropolis.app.model.MovieDetailView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.map
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.detail.MovieDetailViewProvider
import movie.style.EllipsisText
import movie.style.layout.PreviewLayout
import movie.style.textPlaceholder

@Composable
fun MovieDetailColumn(
    detail: Loadable<MovieDetailView>,
    modifier: Modifier = Modifier,
) {
    MovieDetailLayout(
        modifier = modifier,
        title = {
            detail.map { it.name }
                .onLoading { Text("#".repeat(10), Modifier.textPlaceholder(true)) }
                .onSuccess { Text(it) }
        },
        details = {
            detail.map { "%s • %s • %s".format(it.duration, it.countryOfOrigin, it.releasedAt) }
                .onLoading { Text("#".repeat(22), Modifier.textPlaceholder(true)) }
                .onSuccess { Text(it) }
        },
        directors = {
            detail.map { it.directors.joinToString() }
                .onLoading { Text("#".repeat(13), Modifier.textPlaceholder(true)) }
                .onSuccess { Text(it) }
        },
        cast = {
            detail.map { it.cast.joinToString() }
                .onLoading { Text("#".repeat(28), Modifier.textPlaceholder(true)) }
                .onSuccess { EllipsisText(it, 3) }
        }
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun MovieDetailLayoutPreview(
    @PreviewParameter(MovieDetailColumnParameter::class)
    parameter: Loadable<MovieDetailView>
) = PreviewLayout {
    MovieDetailColumn(parameter)
}

private class MovieDetailColumnParameter :
    CollectionPreviewParameterProvider<Loadable<MovieDetailView>>(
        listOf(
            Loadable.loading(),
            Loadable.failure(Throwable()),
            Loadable.success(MovieDetailViewProvider().values.first())
        )
    )