@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@file:Suppress("FunctionName")

package movie.metropolis.app.screen.listing

import android.content.Context
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import movie.metropolis.app.R
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.metropolis.app.screen.home.component.SectionHeadline
import movie.metropolis.app.screen.listing.component.MoviePromo
import movie.metropolis.app.screen.listing.component.MovieRow
import movie.style.CenterAlignedTabRow
import movie.style.Tab
import movie.style.layout.plus
import movie.style.state.ImmutableList.Companion.immutable
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
        item {
            MoviePromo(
                items = promotions,
                onClick = { onClick(it, upcoming) }
            )
        }
        groups.onSuccess {
            ListingLoadedContent(
                it = it,
                context = context,
                upcoming = upcoming,
                onClickFavorite = onClickFavorite,
                onClick = onClick
            )
        }.onLoading {
            ListingLoadingContent()
        }.onFailure {
            ListingErrorContent(it)
        }
    }
}

fun LazyListScope.ListingLoadingContent() {
    item(key = "current-title") {
        SectionHeadline(
            name = "#".repeat(10),
            modifier = Modifier
                .textPlaceholder()
                .animateItemPlacement()
        )
    }
    item(key = "current-content") {
        MovieRow(
            modifier = Modifier.animateItemPlacement(),
            items = Loadable.loading(),
            isShowing = true,
            onClickFavorite = {},
            onClick = {}
        )
    }
}

fun LazyListScope.ListingLoadedContent(
    it: Map<Genre, List<MovieView>>,
    context: Context,
    upcoming: Boolean,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String, upcoming: Boolean) -> Unit
) {
    for ((genre, items) in it) {
        item {
            SectionHeadline(
                modifier = Modifier.animateItemPlacement(),
                name = genre.getName(context)
            )
        }
        item {
            MovieRow(
                modifier = Modifier.animateItemPlacement(),
                items = Loadable.success(items.immutable()),
                isShowing = !upcoming,
                onClickFavorite = onClickFavorite,
                onClick = { onClick(it, upcoming) }
            )
        }
    }
}

fun LazyListScope.ListingErrorContent(throwable: Throwable) {
    item(key = "current-content") {
        MovieRow(
            modifier = Modifier.animateItemPlacement(),
            items = Loadable.failure(throwable),
            isShowing = true,
            onClickFavorite = {},
            onClick = {}
        )
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