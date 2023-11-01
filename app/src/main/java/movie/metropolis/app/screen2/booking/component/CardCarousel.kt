@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.booking.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.util.interpolatePage
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.theme.Theme

@Composable
fun CardCarousel(
    color: Color,
    state: PagerState,
    key: (Int) -> Any,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (Int) -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    HorizontalPager(
        modifier = Modifier.weight(1f),
        state = state,
        key = key,
        contentPadding = contentPadding + PaddingValues(horizontal = 64.dp),
        pageSpacing = 16.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .interpolatePage(
                    state,
                    it,
                    rotationZ = 2.5f,
                    rotationY = 10f,
                    offset = DpOffset.Zero,
                    blur = 32.dp
                ),
            propagateMinConstraints = true
        ) {
            content(it)
        }
    }
    PageIndicator(
        modifier = Modifier.offset(y = -contentPadding.calculateBottomPadding() + 16.dp),
        state = state,
        color = color
    )
}

fun DpOffset.toOffset(density: Density) = with(density) { Offset(x.toPx(), y.toPx()) }

@Composable
fun PageIndicator(
    state: PagerState,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.emphasis.secondary,
    itemSize: Dp = 6.dp,
    itemPadding: Dp = 8.dp,
    displayItems: Int = 5,
    activeItemScale: Float = 1.5f,
) {
    val itemOffsetCount = displayItems / 2
    val listState = rememberLazyListState()
    LaunchedEffect(state.currentPage) {
        listState.animateScrollToItem(state.currentPage)
    }
    LazyRow(
        modifier = modifier
            .width(activeItemScale * itemSize + (displayItems - 1) * itemPadding + (displayItems - 1) * itemSize),
        userScrollEnabled = false,
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(itemPadding)
    ) {

        @Composable
        fun Indicator(selected: Boolean) {
            val scale by animateFloatAsState(if (selected) activeItemScale else 1f)
            val color by animateColorAsState(if (selected) color else LocalContentColor.current)
            Box(
                modifier = Modifier
                    .size(itemSize)
                    .scale(scale)
                    .background(color, CircleShape)
            )
        }

        repeat(itemOffsetCount) { item { Spacer(modifier = Modifier.size(itemSize)) } }

        for (i in 0..<state.pageCount) item {
            Indicator(i == state.currentPage)
        }

        repeat(itemOffsetCount) { item { Spacer(modifier = Modifier.size(itemSize)) } }
    }
}

@Preview
@Composable
private fun CardCarouselPreview() = PreviewLayout {
    CardCarousel(
        color = Color.Blue,
        modifier = Modifier.fillMaxSize(),
        state = rememberPagerState { 4 },
        key = { it }
    ) {
        Box(Modifier.background(Color.Blue))
    }
}