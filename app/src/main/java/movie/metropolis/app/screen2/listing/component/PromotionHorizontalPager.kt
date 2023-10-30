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
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.theme.Theme
import kotlin.math.absoluteValue
import kotlin.math.sign

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

private fun Modifier.interpolatePage(state: PagerState, page: Int) = composed {
    val offset = with(state) { (currentPage - page) + currentPageOffsetFraction }
    val pageOffset = offset.absoluteValue
    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
    val sign = offset.sign
    val density = LocalDensity.current
    val yOffsetPx = with(density) { 16.dp.toPx() }
    graphicsLayer {
        rotationZ = lerp(-sign * 15f, 0f, fraction)
        alpha = lerp(.5f, 1f, fraction)
        scaleX = lerp(.8f, 1f, fraction)
        scaleY = scaleX
        translationY = lerp(yOffsetPx * 2, 0f, fraction)
        translationX = lerp(sign * yOffsetPx, 0f, fraction)
    }
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