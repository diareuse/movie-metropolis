package movie.metropolis.app.screen.cinema

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.textPlaceholder

@Composable
fun CinemaItem(
    name: String,
    address: List<String>,
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
        name = "My Awesome Cinema",
        address = listOf("4th Avenue @ 6th floor"),
        city = "City of Wonders",
        distance = null,
        modifier = modifier,
        textModifier = Modifier.textPlaceholder(true)
    )
}

@Composable
private fun CinemaItemLayout(
    name: String,
    address: List<String>,
    city: String,
    distance: String?,
    modifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        shape = MaterialTheme.shapes.large,
        tonalElevation = 1.dp
    ) {
        Box(
            modifier = Modifier.clickable(
                enabled = onClick != null,
                onClick = onClick ?: {}
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
                    style = MaterialTheme.typography.titleLarge
                )
                if (city !in name) Text(
                    modifier = textModifier,
                    text = city
                )
                Text(
                    modifier = textModifier,
                    text = address.filter { it !in name }.joinToString("\n"),
                    style = MaterialTheme.typography.bodyMedium
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
            address = listOf("Yes at 23/3", "4th floor"),
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
            address = listOf("Yes at 23/3", "4th floor"),
            city = "Warsaw",
            distance = "12.3km",
            onClick = {}
        )
    }
}