@file:OptIn(ExperimentalFoundationApi::class)

package ui.style.widget

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
import ui.style.PreviewLayout
import androidx.compose.foundation.pager.HorizontalPager as FoundationHorizontalPager

@Composable
fun HorizontalPager(
    state: PagerState,
    color: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    cornerSize: Dp = 24.dp,
    contentPadding: Dp = 8.dp,
    content: @Composable (Int) -> Unit
) {
    val outsideShape = RoundedCornerShape(cornerSize + contentPadding)
    val insideShape = RoundedCornerShape(cornerSize)
    val color by animateColorAsState(color)
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FoundationHorizontalPager(
            modifier = Modifier
                .background(color, outsideShape)
                .clip(outsideShape),
            state = state
        ) {
            Box(
                modifier = Modifier
                    .padding(contentPadding)
                    .shadow(cornerSize * 2)
                    .background(contentColor, insideShape)
                    .clip(insideShape),
                propagateMinConstraints = true
            ) {
                content(it)
            }
        }
        HorizontalPagerIndicator(
            state = state,
            colors = PagerIndicatorDefaults.colors(active = color)
        )
    }
}

@Composable
fun <T> HorizontalPager(
    items: List<T>,
    color: Color,
    contentColor: Color,
    modifier: Modifier = Modifier,
    cornerSize: Dp = 24.dp,
    contentPadding: Dp = 8.dp,
    content: @Composable (T) -> Unit
) {
    val state = rememberPagerState { items.size }
    HorizontalPager(
        modifier = modifier,
        state = state,
        color = color,
        contentColor = contentColor,
        cornerSize = cornerSize,
        contentPadding = contentPadding
    ) {
        content(items[it])
    }
}

@Composable
fun HorizontalPagerIndicator(
    state: PagerState,
    modifier: Modifier = Modifier,
    colors: PagerIndicatorColors = PagerIndicatorDefaults.colors(),
) {

    @Composable
    fun Indicator(selected: Boolean) {
        val scale by animateFloatAsState(targetValue = if (selected) 1.5f else 1f)
        val color by animateColorAsState(targetValue = if (selected) colors.active else colors.inactive)
        Box(
            Modifier
                .scale(scale)
                .size(8.dp)
                .background(color, CircleShape)
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(state.pageCount) {
            Indicator(selected = it == state.currentPage)
        }
    }
}

data class PagerIndicatorColors(
    val active: Color,
    val inactive: Color
)

object PagerIndicatorDefaults {
    @Composable
    fun colors(
        active: Color = MaterialTheme.colorScheme.onSurface,
        inactive: Color = active.copy(alpha = .3f)
    ) = PagerIndicatorColors(
        active = active,
        inactive = inactive
    )
}

@Preview(showBackground = true)
@Composable
private fun HorizontalPagerPreview(
    @PreviewParameter(HorizontalPagerParameter::class)
    parameter: HorizontalPagerParameter.Data
) = PreviewLayout(Modifier.padding(16.dp)) {
    val colors = remember { listOf(Color.Blue, Color.Cyan, Color.Magenta) }
    val state = rememberPagerState { colors.size }
    HorizontalPager(state, colors[state.currentPage], Color.White) {
        Box(
            Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
        )
    }
}

private class HorizontalPagerParameter : PreviewParameterProvider<HorizontalPagerParameter.Data> {
    override val values = sequence { yield(Data()) }

    class Data
}