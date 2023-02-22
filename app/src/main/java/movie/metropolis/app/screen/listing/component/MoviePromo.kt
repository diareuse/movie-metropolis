package movie.metropolis.app.screen.listing.component

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerScope
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import movie.metropolis.app.R
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.style.haptic.TickOnChange
import movie.style.layout.EmptyShapeLayout
import movie.style.modifier.overlay
import movie.style.theme.Theme
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MoviePromo(
    items: Loadable<List<MovieView>>,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    state: PagerState = rememberPagerState(),
) {
    TickOnChange(value = state.currentPage)
    items.onLoading {
        HorizontalPager(
            modifier = modifier,
            count = 3,
            userScrollEnabled = false,
            state = rememberPagerState(),
            contentPadding = PaddingValues(start = 24.dp, end = 64.dp, top = 32.dp, bottom = 32.dp),
        ) { index ->
            MoviePoster(
                url = null,
                modifier = Modifier
                    .padding(end = 16.dp)
                    .fillMaxWidth()
                    .aspectRatio(1.5f)
                    .interpolateSize(this, index, Theme.container.poster)
            )
        }
    }.onSuccess {
        val list = items.getOrNull().orEmpty()
        HorizontalPager(
            modifier = modifier,
            count = list.size,
            key = { list[it].id },
            contentPadding = PaddingValues(start = 24.dp, end = 64.dp, top = 32.dp, bottom = 32.dp),
            state = state
        ) { index ->
            val item = it[index]
            var showPopup by remember { mutableStateOf(false) }
            LaunchedEffect(item.id) {
                item.markSeen()
            }
            Box {
                MoviePoster(
                    url = item.poster?.url,
                    modifier = Modifier
                        .padding(end = 16.dp)
                        .detectLongPress { showPopup = true }
                        .fillMaxWidth()
                        .aspectRatio(item.poster?.aspectRatio ?: 1.5f)
                        .interpolateSize(
                            scope = this@HorizontalPager,
                            page = index,
                            shape = Theme.container.poster,
                            shadowColor = animateColorAsState(
                                item.poster?.spotColor ?: Color.Black
                            ).value
                        )
                        .overlay(colorBottom = Color.Black),
                    onClick = { onClick(item.id) },
                    onLongPress = { showPopup = it }
                )
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(24.dp)
                ) {
                    Text(item.name, style = Theme.textStyle.title, color = Color.White)
                }
            }
            MoviePopup(
                isVisible = showPopup,
                onVisibilityChanged = { showPopup = false },
                url = item.posterLarge?.url.orEmpty(),
                year = item.releasedAt,
                director = item.directors.joinToString(),
                name = item.name,
                aspectRatio = item.posterLarge?.aspectRatio ?: DefaultPosterAspectRatio
            )
        }
    }.onEmpty {
        HorizontalPager(
            modifier = modifier,
            count = 1,
            userScrollEnabled = false,
            state = rememberPagerState(),
            contentPadding = PaddingValues(horizontal = 64.dp, vertical = 32.dp)
        ) { _ ->
            EmptyShapeLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                shape = Theme.container.poster
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text("🤫", style = Theme.textStyle.title.copy(fontSize = 48.sp))
                    Text(
                        stringResource(R.string.empty_movie_main_title),
                        style = Theme.textStyle.title,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }.onFailure {
        HorizontalPager(
            modifier = modifier,
            count = 1,
            userScrollEnabled = false,
            state = rememberPagerState(),
            contentPadding = PaddingValues(horizontal = 64.dp, vertical = 32.dp)
        ) { _ ->
            EmptyShapeLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),
                shape = Theme.container.poster,
                color = Theme.color.container.error
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
                ) {
                    Text("😢", style = Theme.textStyle.title.copy(fontSize = 48.sp))
                    Text(
                        stringResource(R.string.error_movie_main_title),
                        style = Theme.textStyle.title,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
private fun Modifier.interpolateSize(
    scope: PagerScope,
    page: Int,
    shape: Shape,
    shadowColor: Color = Color.Black,
) = run {
    val offset = scope.calculateCurrentOffsetForPage(page)
    val pageOffset = offset.absoluteValue
    val fraction = 1f - pageOffset.coerceIn(0f, 1f)
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
    graphicsLayer {
        this.alpha = alpha
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
        MoviePromo(
            items = Loadable.loading(),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
private fun PreviewEmpty() {
    Theme {
        MoviePromo(
            items = Loadable.success(emptyList()),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview(showBackground = true)
@Composable
private fun PreviewError() {
    Theme {
        MoviePromo(
            items = Loadable.failure(Throwable()),
            onClick = {},
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}