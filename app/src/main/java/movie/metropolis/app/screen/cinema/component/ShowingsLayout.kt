package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.theme.Theme

@Composable
fun ShowingsLayout(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 24.dp)) {
            ProvideTextStyle(Theme.textStyle.headline.copy(fontWeight = FontWeight.Bold)) {
                title()
            }
        }
        Surface(
            tonalElevation = 4.dp,
            shape = Theme.container.card
        ) {
            content()
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun MovieShowingLayoutPreview(
    @PreviewParameter(MovieShowingLayoutParameter::class, 1)
    parameter: MovieShowingLayoutParameter.Data
) = PreviewLayout {
    ShowingsLayout(
        title = { Text(parameter.title) }
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(24.dp)
                .background(Theme.color.container.primary)
        )
    }
}

private class MovieShowingLayoutParameter :
    CollectionPreviewParameterProvider<MovieShowingLayoutParameter.Data>(listOf(Data())) {
    data class Data(
        val title: String = "Movie title"
    )
}