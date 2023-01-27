package movie.metropolis.app.screen.cinema

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movie.metropolis.app.R
import movie.style.haptic.withHaptics
import movie.style.layout.EmptyShapeLayout
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.textPlaceholder
import movie.style.theme.Theme

@Composable
fun CinemaItem(
    name: String,
    address: ImmutableList<String>,
    city: String,
    distance: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CinemaItemLayout(
        modifier = modifier,
        name = name,
        address = address,
        city = city,
        distance = distance,
        onClick = onClick
    )
}

@Composable
fun CinemaItem(modifier: Modifier = Modifier) {
    CinemaItemLayout(
        name = "#".repeat(17),
        address = listOf("#".repeat(22)).immutable(),
        city = "#".repeat(15),
        distance = null,
        modifier = modifier,
        textModifier = Modifier.textPlaceholder(true)
    )
}

@Composable
fun CinemaItemEmpty(
    modifier: Modifier = Modifier,
) {
    EmptyShapeLayout(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            Text("📽", style = Theme.textStyle.title.copy(fontSize = 48.sp))
            Text(stringResource(R.string.empty_cinema), textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun CinemaItemLayout(
    name: String,
    address: ImmutableList<String>,
    city: String,
    distance: String?,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = Theme.color.container.background,
        contentColor = Theme.color.content.background,
        shape = Theme.container.card,
        tonalElevation = 2.dp
    ) {
        Box(
            modifier = Modifier.clickable(
                enabled = onClick != null,
                onClick = onClick?.withHaptics() ?: {}
            )
        ) {
            Image(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.BottomEnd)
                    .offset(24.dp, 24.dp)
                    .alpha(.1f),
                painter = painterResource(id = R.drawable.ic_cinema),
                contentDescription = null,
                colorFilter = ColorFilter.tint(LocalContentColor.current)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp, 16.dp)
            ) {
                Text(
                    modifier = textModifier,
                    text = name,
                    style = Theme.textStyle.title
                )
                if (city !in name) Text(
                    modifier = textModifier,
                    text = city
                )
                Text(
                    modifier = textModifier,
                    text = address.filter { it !in name }.joinToString("\n"),
                    style = Theme.textStyle.body
                )
                if (distance != null)
                    Text(
                        modifier = textModifier,
                        text = distance
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        CinemaItem(
            name = "Cinema Something Something",
            address = listOf("Yes at 23/3", "4th floor").immutable(),
            city = "Warsaw",
            distance = "12.3km",
            onClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() {
    Theme {
        CinemaItem(
            name = "Cinema Something Something",
            address = listOf("Yes at 23/3", "4th floor").immutable(),
            city = "Warsaw",
            distance = "12.3km",
            onClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmpty() {
    Theme {
        CinemaItemEmpty(modifier = Modifier.padding(16.dp))
    }
}