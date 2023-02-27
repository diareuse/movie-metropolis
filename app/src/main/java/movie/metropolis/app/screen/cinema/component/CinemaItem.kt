package movie.metropolis.app.screen.cinema.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.AppImage
import movie.style.layout.CutoutLayout
import movie.style.layout.EmptyShapeLayout
import movie.style.layout.PreviewLayout
import movie.style.modifier.optional
import movie.style.modifier.surface
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
    image: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CinemaItemLayout(
        modifier = modifier,
        name = { Text(name) },
        address = { Text(address.filter { it !in name }.joinToString("\n")) },
        city = { if (city !in name) Text(city) },
        distance = {
            if (distance != null) Text(distance, modifier = Modifier.padding(16.dp, 8.dp))
        },
        image = { AppImage(modifier = Modifier.fillMaxSize(), url = image) },
        onClick = onClick
    )
}

@Composable
fun CinemaItem(modifier: Modifier = Modifier) {
    CinemaItemLayout(
        name = { Text("#".repeat(17), modifier = Modifier.textPlaceholder(true)) },
        address = { Text("#".repeat(22), modifier = Modifier.textPlaceholder(true)) },
        city = { Text("#".repeat(15), modifier = Modifier.textPlaceholder(true)) },
        distance = {},
        modifier = modifier,
        image = { AppImage(modifier = Modifier.fillMaxSize(), url = null) }
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
    image: @Composable () -> Unit,
    city: @Composable () -> Unit,
    name: @Composable () -> Unit,
    address: @Composable () -> Unit,
    distance: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null
) {
    CutoutLayout(
        modifier = modifier.wrapContentHeight(unbounded = true),
        color = Theme.color.container.primary,
        shape = Theme.container.button,
        overlay = distance
    ) {
        Column(
            modifier = Modifier
                .optional(onClick) { clickable(onClick = it) }
                .surface(tonalElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .surface(
                        Theme.color.container.surface,
                        Theme.container.card.copy(
                            topStart = CornerSize(0f),
                            topEnd = CornerSize(0f)
                        )
                    )
            ) {
                image()
            }
            Column(
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 16.dp, bottom = 24.dp)
            ) {
                ProvideTextStyle(Theme.textStyle.title) {
                    name()
                }
                ProvideTextStyle(Theme.textStyle.caption) {
                    city()
                    address()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() = PreviewLayout {
    CinemaItem(
        name = "Cinema Something Something",
        address = listOf("Yes at 23/3", "4th floor").immutable(),
        city = "Warsaw",
        distance = "12.3km",
        image = "image.org",
        onClick = {}
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewNight() = PreviewLayout {
    CinemaItem(
        name = "Cinema Something Something",
        address = listOf("Yes at 23/3", "4th floor").immutable(),
        city = "Warsaw",
        distance = "12.3km",
        image = "image.org",
        onClick = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PreviewEmpty() = PreviewLayout {
    CinemaItemEmpty()
}