package movie.metropolis.app.screen.listing

import android.Manifest
import android.content.res.Configuration
import android.os.Build
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
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.model.Genre
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.metropolis.app.screen.home.component.SectionHeadline
import movie.metropolis.app.screen.listing.component.CenterAlignedTabRow
import movie.metropolis.app.screen.listing.component.MoviePromo
import movie.metropolis.app.screen.listing.component.MovieRow
import movie.metropolis.app.screen.listing.component.Tab
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.textPlaceholder
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    behavior: TopAppBarScrollBehavior,
    onClickMovie: (String, upcoming: Boolean) -> Unit,
    profileIcon: @Composable () -> Unit,
    state: LazyListState,
    viewModel: ListingViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val promotions by viewModel.promotions.collectAsState()
    val groups by viewModel.groups.collectAsState()
    val selectedType by viewModel.selectedType.collectAsState()
    val scope = rememberCoroutineScope()
    ListingScreenContent(
        promotions = promotions,
        groups = groups,
        selectedType = selectedType,
        onSelectedTypeChange = { viewModel.selectedType.value = it },
        behavior = behavior,
        state = state,
        profileIcon = profileIcon,
        onClickFavorite = {
            scope.launch {
                val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    actions.requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
                } else {
                    true
                }
                if (!granted) return@launch
                viewModel.toggleFavorite(it)
            }
        },
        onClick = onClickMovie,
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun ListingScreenContent(
    promotions: Loadable<List<MovieView>>,
    groups: Loadable<Map<Genre, List<MovieView>>>,
    selectedType: ShowingType,
    onSelectedTypeChange: (ShowingType) -> Unit,
    profileIcon: @Composable () -> Unit,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String, upcoming: Boolean) -> Unit,
    modifier: Modifier = Modifier,
    behavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
    state: LazyListState = rememberLazyListState()
) = Scaffold(
    modifier = modifier,
    topBar = {
        HomeScreenToolbar(
            profileIcon = profileIcon,
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
        contentPadding = padding,
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
        item { MoviePromo(items = promotions, onClick = { onClick(it, upcoming) }) }
        groups.onSuccess {
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
        }.onLoading {
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
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }.onFailure {
            item(key = "current-content") {
                MovieRow(
                    modifier = Modifier.animateItemPlacement(),
                    items = Loadable.failure(it),
                    isShowing = true,
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun ListingScreenPreview() = Theme {
    ListingScreenContent(
        promotions = Loadable.loading(),
        groups = Loadable.loading(),
        selectedType = ShowingType.Upcoming,
        onSelectedTypeChange = {},
        profileIcon = { /*TODO*/ },
        onClickFavorite = {},
        onClick = { _, _ -> }
    )
}