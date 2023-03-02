package movie.metropolis.app.screen.detail.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.detail.component.MovieDetailLayoutParameter.Data
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun MovieDetailLayout(
    title: @Composable () -> Unit,
    details: @Composable () -> Unit,
    directors: @Composable () -> Unit,
    cast: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ProvideTextStyle(Theme.textStyle.title) {
            title()
        }
        Spacer(Modifier.height(8.dp))
        ProvideTextStyle(Theme.textStyle.caption) {
            details()
            Spacer(Modifier.height(8.dp))
            directors()
            cast()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieDetailLayoutPreview(
    @PreviewParameter(MovieDetailLayoutParameter::class)
    parameter: Data
) = PreviewLayout {
    MovieDetailLayout(
        title = { Text(parameter.title) },
        details = { Text(parameter.details) },
        directors = { Text(parameter.directors) },
        cast = { Text(parameter.cast) }
    )
}

private class MovieDetailLayoutParameter : CollectionPreviewParameterProvider<Data>(
    listOf(Data())
) {
    data class Data(
        val title: String = "Movie title",
        val details: String = "2h30m • USA • 2023",
        val directors: String = "Foo bar",
        val cast: String = "Foobar Foofoo"
    )
}