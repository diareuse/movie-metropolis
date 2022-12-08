package movie.metropolis.app.screen.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaBookingView
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.textPlaceholder

@Composable
fun ShowingItem(
    title: String,
    showings: Map<CinemaBookingView.LanguageAndType, List<CinemaBookingView.Availability>>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShowingLayout(
        modifier = modifier,
        items = showings,
        key = { it.id },
        title = { Text(title) },
        section = { ShowingItemSection(type = it.type, language = it.language) }
    ) {
        ShowingItemTime(
            modifier = Modifier.clickable { onClick(it.url) },
            time = it.startsAt
        )
    }
}

@Composable
fun ShowingItem(
    modifier: Modifier = Modifier
) {
    ShowingLayout(
        modifier = modifier,
        items = mapOf(
            "a" to List(3) { it },
            "b" to List(1) { it },
            "c" to List(2) { it },
        ),
        key = { it },
        title = { Text("My super awesome cinema", Modifier.textPlaceholder(true)) },
        section = { ShowingItemSection(type = "type", language = "English", isLoading = true) }
    ) {
        ShowingItemTime(
            modifier = Modifier.textPlaceholder(true),
            time = "10.00"
        )
    }
}

@Composable
fun ShowingItemSection(type: String, language: String, isLoading: Boolean = false) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .textPlaceholder(isLoading),
                painter = painterResource(id = R.drawable.ic_screening_type),
                contentDescription = null
            )
            Text(
                modifier = Modifier.textPlaceholder(isLoading),
                text = type
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier
                    .size(16.dp)
                    .textPlaceholder(isLoading),
                painter = painterResource(id = R.drawable.ic_language),
                contentDescription = null
            )
            Text(
                modifier = Modifier.textPlaceholder(isLoading),
                text = language,
                style = MaterialTheme.typography.bodySmall
            )
        }
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

@Composable
fun <T, T2> ShowingLayout(
    items: Map<T2, List<T>>,
    key: (T) -> Any,
    title: @Composable () -> Unit,
    section: @Composable (T2) -> Unit,
    modifier: Modifier = Modifier,
    background: (@Composable () -> Unit)? = null,
    item: @Composable (T) -> Unit,
) {
    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(bottom = 8.dp)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            ) {
                title()
            }
        }
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            tonalElevation = if (background == null) 1.dp else 0.dp
        ) {
            Box {
                val (size, onSizeChanged) = remember { mutableStateOf(IntSize.Zero) }
                val density = LocalDensity.current
                Box(
                    modifier = Modifier.size(
                        width = with(density) { size.width.toDp() },
                        height = with(density) { size.height.toDp() }
                    )
                ) {
                    background?.invoke()
                }
                Column(
                    modifier = Modifier
                        .onSizeChanged(onSizeChanged)
                        .padding(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    for ((label, collection) in items) Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(Modifier.padding(horizontal = 16.dp)) {
                            CompositionLocalProvider(
                                LocalTextStyle provides MaterialTheme.typography.titleMedium
                            ) {
                                section(label)
                            }
                        }
                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) {
                            items(collection, key) {
                                item(it)
                            }
                        }
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
            items = mapOf(
                ("2D" to "English (Czech)") to listOf("11:11", "12:34", "22:22"),
                ("3D" to "English") to listOf("11:11", "12:34", "22:22"),
            ),
            key = { it },
            title = { Text("My Cinema") },
            section = { ShowingItemSection(type = it.first, language = it.second) }
        ) {
            ShowingItemTime(time = it)
        }
    }
}