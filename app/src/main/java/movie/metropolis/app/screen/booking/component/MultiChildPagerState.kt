@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.pager.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*

class MultiChildPagerState(
    initialPage: Int,
    initialPageOffsetFraction: Float,
    updatedPageCount: () -> Int,
    private vararg val children: PagerState
) : PagerState(initialPage, initialPageOffsetFraction) {

    var pageCountState = mutableStateOf(updatedPageCount)
    override val pageCount: Int get() = pageCountState.value.invoke()

    override suspend fun scroll(
        scrollPriority: MutatePriority,
        block: suspend ScrollScope.() -> Unit
    ) {
        super.scroll(scrollPriority) {
            val scope = ScrollScope { pixels ->
                scrollBy(pixels).also { _ ->
                    val currentPage = currentPage
                    val currentPageOffsetFraction = currentPageOffsetFraction
                    for (child in children) with(child) {
                        updateCurrentPage(currentPage, currentPageOffsetFraction)
                    }
                }
            }
            scope.block()
        }
    }

    override fun dispatchRawDelta(delta: Float): Float {
        return super.dispatchRawDelta(delta).also {
            for (child in children)
                child.dispatchRawDelta(delta)
        }
    }

    private fun ScrollScope(scrollBy: (Float) -> Float) = object : ScrollScope {
        override fun scrollBy(pixels: Float): Float {
            return scrollBy(pixels)
        }
    }

    operator fun component1() = this
    operator fun component2() = children[0]

}

@Composable
fun rememberMultiChildPagerState(
    childCount: Int,
    initialPage: Int = 0,
    initialPageOffsetFraction: Float = 0f,
    pageCount: () -> Int
): MultiChildPagerState {
    val children = Array(childCount) {
        rememberPagerState(pageCount = pageCount)
    }
    return rememberSaveable(saver = rememberMultiChildPagerStateSaver(children)) {
        MultiChildPagerState(
            initialPage = initialPage,
            initialPageOffsetFraction = initialPageOffsetFraction,
            updatedPageCount = pageCount,
            children = children
        )
    }.apply {
        pageCountState.value = pageCount
    }
}

@Composable
fun rememberMultiChildPagerStateSaver(children: Array<out PagerState>) = remember(children) {
    listSaver<MultiChildPagerState, Any>(
        save = {
            listOf(
                it.currentPage,
                it.currentPageOffsetFraction,
                it.pageCount
            )
        },
        restore = {
            MultiChildPagerState(
                initialPage = it[0] as Int,
                initialPageOffsetFraction = it[1] as Float,
                updatedPageCount = { it[2] as Int },
                children = children
            )
        }
    )
}