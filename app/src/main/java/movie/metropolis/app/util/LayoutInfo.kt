package movie.metropolis.app.util

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.util.*

private operator fun IntRange.contains(other: IntRange): Boolean {
    return first <= other.first && last >= other.last
}

val LazyStaggeredGridLayoutInfo.completelyVisibleItemsInfo: List<LazyStaggeredGridItemInfo>
    get() {
        fun LazyStaggeredGridItemInfo.offset() = when (orientation) {
            Orientation.Vertical -> offset.y..offset.y + size.height
            Orientation.Horizontal -> offset.x..offset.x + size.width
        }

        val visibleOffset = viewportStartOffset..viewportEndOffset
        return visibleItemsInfo.fastFilter { it.offset() in visibleOffset }
    }

val LazyListLayoutInfo.completelyVisibleItemsInfo: List<LazyListItemInfo>
    get() {
        val visibleOffset = viewportStartOffset..viewportEndOffset
        fun LazyListItemInfo.offset() = offset + size
        return visibleItemsInfo.fastFilter { it.offset() in visibleOffset }
    }

@Composable
fun LazyListState.rememberVisibleItemAsState(): State<Int> {
    val visibleItems by remember { derivedStateOf { layoutInfo.completelyVisibleItemsInfo.fastMap { it.index } } }
    val item = rememberSaveable { mutableIntStateOf(visibleItems.randomOrNull() ?: -1) }
    LaunchedEffect(item.value !in visibleItems) {
        val info = visibleItems
        if (item.value !in info)
            item.value = info.randomOrNull() ?: -1
    }
    return item
}

@Composable
fun LazyStaggeredGridState.rememberVisibleItemAsState(): State<Int> {
    val visibleItems by remember { derivedStateOf { layoutInfo.completelyVisibleItemsInfo.fastMap { it.index } } }
    val item = rememberSaveable { mutableIntStateOf(visibleItems.randomOrNull() ?: -1) }
    LaunchedEffect(item.value !in visibleItems) {
        val info = visibleItems
        if (item.value !in info)
            item.value = info.randomOrNull() ?: -1
    }
    return item
}