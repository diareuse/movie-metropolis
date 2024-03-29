@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.ticket.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen.booking.component.rememberMultiChildPagerState
import movie.metropolis.app.util.interpolatePage
import movie.style.haptic.TickOnChange
import movie.style.layout.PreviewLayout
import movie.style.layout.plus
import movie.style.theme.Theme

@Composable
fun CardCarousel(
    state: PagerState,
    key: (Int) -> Any,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    content: @Composable (Int) -> Unit,
) = HorizontalPager(
    modifier = modifier,
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
                blur = 32.dp,
                alphaRange = 1f..1f
            ),
        propagateMinConstraints = true
    ) {
        content(it)
    }
}

fun DpOffset.toOffset(density: Density) = with(density) { Offset(x.toPx(), y.toPx()) }

@Composable
fun PageIndicator(
    state: PagerState,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.emphasis.secondary,
    itemSize: Dp = 9.dp,
    itemPadding: Dp = 4.dp,
    displayItems: Int = 5
) {
    TickOnChange(state.currentPage)
    HorizontalPager(
        modifier = modifier.width(itemSize * displayItems + itemPadding * (displayItems - 1)),
        state = state,
        userScrollEnabled = false,
        contentPadding = PaddingValues(horizontal = itemSize * (displayItems / 2) + itemPadding * ((displayItems - 1) / 2)),
        pageSpacing = itemPadding
    ) {
        Box(
            modifier = Modifier
                .interpolatePage(
                    state,
                    it,
                    rotationZ = 0f,
                    rotationY = 0f,
                    blur = 0.dp,
                    offset = DpOffset.Zero,
                    scaleRange = .6f..1f,
                    alphaRange = .5f..1f
                )
                .size(itemSize)
                .background(color, CircleShape)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CardCarouselPreview() = PreviewLayout {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val (state, child) = rememberMultiChildPagerState(childCount = 1) { 4 }
        CardCarousel(
            state = state,
            key = { it },
            modifier = Modifier.weight(1f)
        ) {
            Box(Modifier.background(Color.Blue))
        }
        PageIndicator(child)
    }
}