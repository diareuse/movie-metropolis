@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("FunctionName")

package movie.metropolis.app.screen.listing

import android.content.Context
import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.metropolis.app.screen.home.component.SectionHeadline
import movie.metropolis.app.screen.listing.component.DefaultPosterAspectRatio
import movie.metropolis.app.screen.listing.component.MovieItemActive
import movie.metropolis.app.screen.listing.component.MovieItemError
import movie.metropolis.app.screen.listing.component.MovieItemLoading
import movie.metropolis.app.screen.listing.component.MovieItemUpcoming
import movie.metropolis.app.screen.listing.component.MoviePopup
import movie.metropolis.app.screen.listing.component.MoviePoster
import movie.metropolis.app.screen.listing.component.PromoCard
import movie.metropolis.app.screen.listing.component.PromoCardPager
import movie.metropolis.app.screen.listing.component.SimpleRow
import movie.metropolis.app.screen.listing.component.detectLongPress
import movie.style.CenterAlignedTabRow
import movie.style.Image
import movie.style.PaletteImageState
import movie.style.Tab
import movie.style.layout.plus
import movie.style.modifier.overlay
import movie.style.rememberImageState
import movie.style.rememberPaletteImageState
import movie.style.textPlaceholder
import movie.style.theme.Theme

@Composable
fun ListingScreen(
    promotions: Loadable<List<MovieView>>,
    groups: Loadable<Map<Genre, List<MovieView>>>,
    selectedType: ShowingType,
    onSelectedTypeChange: (ShowingType) -> Unit,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String, upcoming: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    behavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    state: LazyListState = rememberLazyListState()
) = Scaffold(
    modifier = modifier,
    topBar = {
        HomeScreenToolbar(
            behavior = behavior,
            title = { Text(stringResource(id = R.string.movies)) }
        )
    }
) { padding ->
    val context = LocalContext.current
    val upcoming = selectedType == ShowingType.Upcoming
    val (background, onBackgroundChange) = remember { mutableStateOf<String?>(null) }
    AnimatedContent(
        modifier = Modifier
            .fillMaxSize()
            .alpha(.4f)
            .blur(16.dp)
            .overlay(Theme.color.container.background),
        targetState = background,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) {
        if (it != null) Image(state = rememberImageState(url = it))
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(behavior.nestedScrollConnection)
            .testTag("listingColumn"),
        contentPadding = padding + contentPadding,
        state = state,
    ) {
        item {
            CenterAlignedTabRow(
                selected = selectedType.ordinal,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    selected = selectedType == ShowingType.Available,
                    onClick = { onSelectedTypeChange(ShowingType.Available) }) {
                    Text(text = stringResource(id = R.string.showing))
                }
                Tab(
                    selected = selectedType == ShowingType.Upcoming,
                    onClick = { onSelectedTypeChange(ShowingType.Upcoming) }) {
                    Text(text = stringResource(id = R.string.upcoming))
                }
            }
        }
        promotions.onSuccess {
            ListingLoadedPromo(it, onBackgroundChange = onBackgroundChange) { (item, state) ->
                var showPopup by remember { mutableStateOf(false) }
                PromoCard(
                    modifier = Modifier.detectLongPress { showPopup = true },
                    label = { Text(item.name) }
                ) {
                    Image(
                        modifier = Modifier
                            .clickable { onClick(item.id, upcoming) }
                            .detectLongPress { showPopup = true },
                        state = state
                    )
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
        }.onLoading {
            ListingLoadingPromo()
        }.onFailure {
            ListingErrorPromo()
        }
        groups.onSuccess {
            ListingLoadedContent(
                it = it,
                context = context
            ) { item ->
                var showPopup by remember { mutableStateOf(false) }
                when {
                    upcoming -> MovieItemUpcoming(
                        view = item,
                        onClick = { onClick(item.id, true) },
                        onClickFavorite = { onClickFavorite(item) },
                        onLongPress = { showPopup = it }
                    )

                    else -> MovieItemActive(
                        view = item,
                        onClick = { onClick(item.id, false) },
                        onLongPress = { showPopup = it }
                    )
                }
                val large = item.posterLarge?.url
                if (large != null) MoviePopup(
                    isVisible = showPopup,
                    onVisibilityChanged = { showPopup = false },
                    url = large,
                    year = item.releasedAt,
                    director = item.directors.joinToString(),
                    name = item.name,
                    aspectRatio = item.posterLarge?.aspectRatio ?: DefaultPosterAspectRatio
                )
            }
        }.onLoading {
            ListingLoadingContent()
        }.onFailure {
            ListingErrorContent()
        }
    }
}

fun LazyListScope.ListingLoadingPromo() = item {
    PromoCardPager(items = List(3) { it }, enabled = false) {
        PromoCard(label = { Text("#".repeat(10), Modifier.textPlaceholder()) }) {
            MoviePoster(null)
        }
    }
}

fun LazyListScope.ListingErrorPromo() = item {
    PromoCardPager(items = List(3) { it }, enabled = false) {
        PromoCard(label = {
            Text(
                text = stringResource(R.string.error_movie_main_title),
                modifier = Modifier.textPlaceholder()
            )
        }) {}
    }
}

fun LazyListScope.ListingLoadedPromo(
    items: List<MovieView>,
    onBackgroundChange: (String?) -> Unit,
    content: @Composable PagerScope.(Pair<MovieView, PaletteImageState>) -> Unit
) = item {
    val items = items.map { it to rememberPaletteImageState(url = it.poster?.url.orEmpty()) }
    val state = rememberPagerState { items.size }
    val currentShadowColor by animateColorAsState(
        targetValue = items[state.currentPage].second.palette.color
    )
    LaunchedEffect(state.currentPage, items) {
        onBackgroundChange(items[state.currentPage].first.poster?.url)
    }
    PromoCardPager(
        items = items,
        shadowColor = currentShadowColor,
        state = state,
        content = content
    )
}

fun LazyListScope.ListingLoadingContent() {
    item {
        SectionHeadline(
            name = "#".repeat(10),
            modifier = Modifier
                .textPlaceholder()
                .animateItemPlacement()
        )
    }
    item {
        SimpleRow(
            items = List(5) { it }
        ) {
            MovieItemLoading()
        }
    }
}

fun LazyListScope.ListingLoadedContent(
    it: Map<Genre, List<MovieView>>,
    context: Context,
    item: @Composable (MovieView) -> Unit,
) {
    for ((genre, items) in it) {
        item {
            SectionHeadline(
                modifier = Modifier.animateItemPlacement(),
                name = genre.getName(context)
            )
        }
        item {
            SimpleRow(
                items = items,
                key = MovieView::id,
                content = item
            )
        }
    }
}

fun LazyListScope.ListingErrorContent() {
    item {
        SimpleRow(items = List(3) { it }) {
            MovieItemError()
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ListingScreenPreview() = Theme {
    ListingScreen(
        promotions = Loadable.loading(),
        groups = Loadable.loading(),
        selectedType = ShowingType.Upcoming,
        onSelectedTypeChange = {},
        onClickFavorite = {},
        onClick = { _, _ -> }
    )
}