package movie.metropolis.app.screen.listing

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.metropolis.app.theme.Theme
import kotlin.math.absoluteValue
import kotlin.math.sign

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MoviePager(
    items: Loadable<List<MovieView>>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
) {
    items.onLoading {
        HorizontalPager(
            modifier = modifier,
            count = 3,
            userScrollEnabled = true,
            state = rememberPagerState(1),
            contentPadding = PaddingValues(start = 64.dp, end = 64.dp, top = 32.dp, bottom = 64.dp)
        ) { index ->
            MoviePoster(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(DefaultPosterAspectRatio)
                    .interpolateSize(this, index, MaterialTheme.shapes.medium),
                url = null,
                onClick = {}
            )
        }
    }.onSuccess {
        val list = items.getOrNull().orEmpty()
        HorizontalPager(
            modifier = modifier,
            count = list.size,
            key = { list[it].id },
            contentPadding = PaddingValues(horizontal = 64.dp, vertical = 32.dp),
            state = state
        ) { index ->
            val item = it[index]
            var color by remember { mutableStateOf(Color.Black) }
            MoviePoster(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(item.poster?.aspectRatio ?: DefaultPosterAspectRatio)
                    .interpolateSize(this, index, MaterialTheme.shapes.medium, shadowColor = color),
                url = item.poster?.url,
                onClick = { onClick(item.id) },
                onSpotColorResolved = { color = it }
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun Modifier.interpolateSize(
    scope: PagerScope,
    page: Int,
    shape: Shape,
    rotationDegrees: Float = 5f,
    translation: Dp = 32.dp,
    shadowColor: Color = Color.Black,
) = run {
    val offset = scope.calculateCurrentOffsetForPage(page)
    val pageOffset = offset.absoluteValue
    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
    val scale = lerp(
        start = 0.85f,
        stop = 1f,
        fraction = fraction
    )
    val alpha = lerp(
        start = 0.5f,
        stop = 1f,
        fraction = fraction
    )
    val shadow = lerp(
        start = 0.dp,
        stop = 32.dp,
        fraction = fraction
    )
    val rotation = lerp(
        start = if (offset.sign == -1f) rotationDegrees.absoluteValue else 0f,
        stop = if (offset.sign == -1f) 0f else -rotationDegrees.absoluteValue,
        fraction = when (offset.sign) {
            -1f -> fraction
            0f -> 0f
            else -> 1f - fraction
        }
    )
    val translation = lerp(
        start = translation,
        stop = 0.dp,
        fraction = fraction
    )
    graphicsLayer {
        this.alpha = alpha
        this.rotationZ = rotation
        this.translationY = translation.toPx()
        this.scaleX = scale
        this.scaleY = scale
        this.shadowElevation = shadow.toPx()
        this.shape = shape
        this.clip = true
        this.ambientShadowColor = shadowColor
        this.spotShadowColor = shadowColor
    }
}

private fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return (1 - fraction) * start + fraction * stop
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MoviePager(
            items = Loadable.loading(),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}