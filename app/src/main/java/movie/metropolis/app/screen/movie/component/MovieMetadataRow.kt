package movie.metropolis.app.screen.movie.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun MovieMetadataRow(
    time: @Composable () -> Unit,
    country: @Composable () -> Unit,
    year: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.container.primary,
    contentColor: Color = Theme.color.content.primary,
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(-(16).dp, Alignment.CenterHorizontally)
) {
    ProvideTextStyle(Theme.textStyle.title.copy(textAlign = TextAlign.Center)) {
        val shape = Theme.container.card
        val background = Theme.color.container.background
        Box(
            modifier = Modifier
                .rotate(-10f)
                .scale(.9f)
                .fillMaxHeight()
                .aspectRatio(1f)
                .surface(
                    color
                        .copy(.2f)
                        .compositeOver(background), shape, 16.dp
                )
                .glow(shape, contentColor),
            contentAlignment = Alignment.Center
        ) {
            time()
        }
        Box(
            modifier = Modifier
                .rotate(5f)
                .zIndex(1f)
                .fillMaxHeight()
                .aspectRatio(1f)
                .surface(color, shape, 16.dp)
                .glow(shape, contentColor),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(LocalContentColor provides contentColor) {
                country()
            }
        }
        Box(
            modifier = Modifier
                .rotate(15f)
                .scale(.9f)
                .fillMaxHeight()
                .aspectRatio(1f)
                .surface(
                    color
                        .copy(.2f)
                        .compositeOver(background), shape, 16.dp
                )
                .glow(shape, contentColor),
            contentAlignment = Alignment.Center
        ) {
            year()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MovieMetadataRowPreview() = PreviewLayout {
    MovieMetadataRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(85.dp),
        time = { Text("1h\n32m") },
        country = { Text("USA") },
        year = { Text("2023") }
    )
}