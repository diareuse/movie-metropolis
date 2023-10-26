package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*

@Composable
fun <T> SimpleRow(
    items: List<T>,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(24.dp),
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(16.dp),
    key: (T) -> Any = { it ?: Unit },
    content: @Composable (item: T) -> Unit,
) = LazyRow(
    modifier = modifier,
    horizontalArrangement = horizontalArrangement,
    contentPadding = contentPadding,
    state = state
) {
    items(items, key = key) { item ->
        content(item)
    }
}