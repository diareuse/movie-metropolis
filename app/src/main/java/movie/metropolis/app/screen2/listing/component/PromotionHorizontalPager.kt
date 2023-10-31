@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen2.listing.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.util.interpolatePage
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.theme.Theme

@Composable
fun PromotionHorizontalPager(
    state: PagerState,
    modifier: Modifier = Modifier,
    content: @Composable (Int) -> Unit,
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally,
    verticalArrangement = Arrangement.spacedBy(8.dp)
) {
    PageIndicator(state = state)
    HorizontalPager(
        state = state
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .interpolatePage(state, it)
                .aspectRatio(1.5f),
            propagateMinConstraints = true
        ) {
            content(it)
        }
    }
}

@Composable
private fun PageIndicator(
    state: PagerState,
    modifier: Modifier = Modifier,
    color: Color = Theme.color.emphasis.secondary,
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp)
) {

    @Composable
    fun Indicator(selected: Boolean) {
        val scale by animateFloatAsState(if (selected) 1.5f else 1f)
        val color by animateColorAsState(if (selected) color else LocalContentColor.current)
        Box(
            modifier = Modifier
                .size(6.dp)
                .scale(scale)
                .background(color, CircleShape)
        )
    }

    for (i in 0..<state.pageCount)
        Indicator(i == state.currentPage)
}

@Preview(showBackground = true)
@Composable
private fun PromotionHorizontalPagerPreview() = PreviewLayout {
    PromotionHorizontalPager(
        state = rememberPagerState(initialPage = 1) { 3 }
    ) {
        Box(Modifier.surface(Color.Blue, RoundedCornerShape(24.dp)))
    }
}