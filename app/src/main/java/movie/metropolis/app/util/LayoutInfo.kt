package movie.metropolis.app.util

import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.staggeredgrid.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.util.*

val LazyStaggeredGridLayoutInfo.completelyVisibleItemsInfo: List<LazyStaggeredGridItemInfo>
    get() {
        val visibleOffset = viewportStartOffset..viewportEndOffset
        return visibleItemsInfo.filter { it.offset.x + it.size.width * 2 in visibleOffset }
    }

val LazyListLayoutInfo.completelyVisibleItemsInfo: List<LazyListItemInfo>
    get() {
        val visibleOffset = viewportStartOffset..viewportEndOffset
        return visibleItemsInfo.filter { it.offset + it.size * 2 in visibleOffset }
    }

@Composable
fun LazyListState.rememberVisibleItemAsState(): State<Int> {
    val layoutInfo by remember { derivedStateOf { layoutInfo } }
    fun select() = layoutInfo.completelyVisibleItemsInfo.randomOrNull()?.index
    val item = rememberSaveable { mutableIntStateOf(select() ?: -1) }
    LaunchedEffect(layoutInfo) {
        val info = layoutInfo.completelyVisibleItemsInfo
        if (item.value !in info.fastMap { it.index })
            item.value = info.randomOrNull()?.index ?: -1
    }
    return item
}

@Composable
fun LazyStaggeredGridState.rememberVisibleItemAsState(): State<Int> {
    val layoutInfo by remember { derivedStateOf { layoutInfo } }
    fun select() = layoutInfo.completelyVisibleItemsInfo.randomOrNull()?.index
    val item = rememberSaveable { mutableIntStateOf(select() ?: -1) }
    LaunchedEffect(layoutInfo) {
        val info = layoutInfo.completelyVisibleItemsInfo
        if (item.value !in info.fastMap { it.index })
            item.value = info.randomOrNull()?.index ?: -1
    }
    return item
}