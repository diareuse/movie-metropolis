@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.listing.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.AppHorizontalPager
import movie.style.haptic.TickOnChange
import movie.style.modifier.overlay
import movie.style.theme.Theme
import movie.style.util.lerp
import kotlin.math.absoluteValue

@Composable
fun <T> PromoCardPager(
    items: List<T>,
    modifier: Modifier = Modifier,
    shadowColor: Color = Color.Black,
    shape: Shape = Theme.container.poster,
    enabled: Boolean = true,
    state: PagerState = rememberPagerState { items.size },
    contentPadding: PaddingValues = PaddingValues(
        start = 24.dp,
        end = 24.dp + 40.dp,
        top = 24.dp,
        bottom = 24.dp
    ),
    content: @Composable PagerScope.(T) -> Unit
) {
    TickOnChange(value = state.currentPage)
    AppHorizontalPager(
        modifier = modifier,
        state = state,
        userScrollEnabled = enabled,
        contentPadding = contentPadding
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .interpolatePageProps(state, it, shape, shadowColor),
            propagateMinConstraints = true
        ) {
            content(items[it])
        }
    }
}

@Composable
fun PromoCard(
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier) {
        Box(
            Modifier
                .matchParentSize()
                .overlay(colorBottom = Color.Black.copy(alpha = .4f)),
            propagateMinConstraints = true
        ) {
            content()
        }
        Box(
            Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            ProvideTextStyle(Theme.textStyle.title) {
                CompositionLocalProvider(LocalContentColor provides Color.White) {
                    label()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
private fun Modifier.interpolatePageProps(
    state: PagerState,
    page: Int,
    shape: Shape,
    shadowColor: Color = Color.Black,
) = run {
    val offset = with(state) { (currentPage - page) + currentPageOffsetFraction }
    val pageOffset = offset.absoluteValue
    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
    val alpha = lerp(
        start = 0.5f,
        stop = 1f,
        fraction = fraction
    )
    val shadow = lerp(
        start = 0.dp,
        stop = 24.dp,
        fraction = fraction
    )
    graphicsLayer {
        this.alpha = alpha
        this.shadowElevation = shadow.toPx()
        this.shape = shape
        this.clip = true
        this.ambientShadowColor = shadowColor
        this.spotShadowColor = shadowColor
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        PromoCardPager(List(3) { 1 }, shadowColor = Color.Magenta) {
            PromoCard(label = { Text("Label") }) {
                Image(
                    modifier = Modifier.background(Color.Magenta),
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            }
        }
    }
}