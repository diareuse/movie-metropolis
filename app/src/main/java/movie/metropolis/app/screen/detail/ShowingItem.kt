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
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.theme.Theme

@Composable
fun ShowingItem(
    title: String,
    showings: Map<String, List<CinemaBookingView.Availability>>,
    onClick: (String) -> Unit
) {
    ShowingLayout(
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
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .then(modifier)
    ) {
        Text(
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            )
        ) {
            title()
        }
        for ((label, collection) in items) Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(label)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(collection, key) {
                    Box(modifier = Modifier.animateItemPlacement()) {
                        item(it)
                    }
                }
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