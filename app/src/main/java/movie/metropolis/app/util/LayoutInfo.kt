package movie.metropolis.app.util

import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.util.*
import kotlinx.coroutines.delay

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
        fun LazyListItemInfo.offset() = (offset + size)
        return visibleItemsInfo.fastFilter { it.offset() in visibleOffset }
    }

@Composable
fun LazyListState.rememberVisibleItemAsState(): State<Int> {
    val item = rememberSaveable { mutableIntStateOf(-1) }
    fun update() {
        val visibleItems = layoutInfo.completelyVisibleItemsInfo.fastMap { it.index }
        if (item.value !in visibleItems) {
            item.value = visibleItems.randomOrNull() ?: -1
        }
    }
    SideEffect {
        update()
    }
    LaunchedEffect(this) {
        while (true) {
            delay(1000)
            update()
        }
    }
    return item
}

@Composable
fun LazyStaggeredGridState.rememberVisibleItemAsState(): State<Int> {
    val item = rememberSaveable { mutableIntStateOf(-1) }
    fun update() {
        val visibleItems = layoutInfo.completelyVisibleItemsInfo.fastMap { it.index }
        if (item.value !in visibleItems) {
            item.value = visibleItems.randomOrNull() ?: -1
        }
    }
    SideEffect {
        update()
    }
    LaunchedEffect(this) {
        while (true) {
            delay(1000)
            update()
        }
    }
    return item
}