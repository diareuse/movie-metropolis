package movie.style.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import movie.style.theme.extendBy

@Composable
fun StackedCardLayout(
    modifier: Modifier = Modifier,
    color: Color = Theme.color.container.secondary,
    contentColor: Color = Theme.color.contentColorFor(color),
    contentPadding: PaddingValues = PaddingValues(),
    headline: @Composable RowScope.() -> Unit,
    background: @Composable BoxScope.() -> Unit = {},
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = color,
        contentColor = contentColor,
        shape = Theme.container.card.extendBy(padding = contentPadding.calculateBottomPadding())
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                CompositionLocalProvider(
                    LocalTextStyle provides Theme.textStyle.body.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                ) {
                    headline()
                }
            }
            Surface(
                modifier = Modifier.padding(contentPadding),
                tonalElevation = 2.dp,
                color = Theme.color.container.background,
                shape = Theme.container.card
            ) {
                Box {
                    background()
                    content()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        StackedCardLayout(headline = { Text("Hello") }) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewPadded() {
    Theme {
        StackedCardLayout(
            headline = { Text("Hello") },
            contentPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
            color = Theme.color.container.error
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        }
    }
}