@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.screen2.ticket.component.PageIndicator
import movie.metropolis.app.util.interpolatePage
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface

@Composable
fun PromotionHorizontalPager(
    state: PagerState,
    indicatorState: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    PageIndicator(
        state = indicatorState
    )
    HorizontalPager(
        state = state
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .interpolatePage(
                    state = state,
                    page = it,
                    rotationZ = 2.5f,
                    rotationY = 10f,
                    offset = DpOffset.Zero,
                    blur = 32.dp
                )
                .aspectRatio(1.5f),
            propagateMinConstraints = true
        ) {
            content(it)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PromotionHorizontalPagerPreview() = PreviewLayout {
    val state = rememberPagerState(initialPage = 1) { 3 }
    PromotionHorizontalPager(
        state = state,
        indicatorState = state
    ) {
        Box(Modifier.surface(Color.Blue, RoundedCornerShape(24.dp)))
    }
}