@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.booking.component

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.pager.*
import androidx.compose.runtime.*

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
                    for (child in children)
                        child.snapToItem(currentPage, currentPageOffsetFraction)
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

    private fun PagerState.snapToItem(page: Int, offset: Float) {
        val actualOffset = offset * pageAvailableSpace.invoke(this) as Int
        snapToItem.invoke(this, page, actualOffset.toInt())
    }

    operator fun component1() = this
    operator fun component2() = children[0]

    companion object {

        private val pagerState = PagerState::class.java
        private val pageAvailableSpace = pagerState.getDeclaredMethod("getPageAvailableSpace")
        private val snapToItem = pagerState.getDeclaredMethod(
            "snapToItem\$foundation_release",
            Int::class.java,
            Int::class.java
        )

        init {
            pageAvailableSpace.isAccessible = true
            snapToItem.isAccessible = true
        }
    }

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
    return remember {
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