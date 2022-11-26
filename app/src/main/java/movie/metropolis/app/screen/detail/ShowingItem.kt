package movie.metropolis.app.screen.detail

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.theme.Theme

@Composable
fun ShowingItem(
    title: String,
    showings: Map<String, List<CinemaBookingView.Availability>>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShowingLayout(
        modifier = modifier,
        items = showings,
        key = { it.id },
        title = { Text(title) }
    ) {
        ShowingItemTime(
            modifier = Modifier.clickable { onClick(it.url) },
            time = it.startsAt
        )
    }
}

@Composable
fun ShowingItemTime(
    time: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.small
    ) {
        Text(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp),
            text = time,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T> ShowingLayout(
    items: Map<String, List<T>>,
    key: (T) -> Any,
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    item: @Composable (T) -> Unit,
) {
    Box(modifier = modifier) {
        var paddingTop by rememberSaveable { mutableStateOf(0) }
        Surface(
            modifier = Modifier
                .padding(top = with(LocalDensity.current) { paddingTop.toDp() })
                .fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(top = with(LocalDensity.current) { paddingTop.toDp() })
                    .padding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for ((label, collection) in items) Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        modifier = Modifier.padding(12.dp, 0.dp),
                        text = label,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(collection, key) {
                            item(it)
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .background(
                    MaterialTheme.colorScheme.surface,
                    CircleShape
                )//MaterialTheme.shapes.small)
                .padding(horizontal = 8.dp, vertical = 2.dp)
                .onSizeChanged { paddingTop = it.height / 2 }
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            ) {
                title()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        ShowingLayout(
            items = mapOf("2D" to listOf("11:11", "12:34", "22:22")),
            key = { it },
            title = { Text("My Cinema") }
        ) {
            ShowingItemTime(time = it)
        }
    }
}