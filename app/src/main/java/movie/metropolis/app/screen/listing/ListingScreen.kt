package movie.metropolis.app.screen.listing

import android.Manifest
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
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
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
import movie.metropolis.app.screen.listing.component.MoviePromo
import movie.metropolis.app.screen.listing.component.MovieRow
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.textPlaceholder
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    padding: PaddingValues,
    behavior: TopAppBarScrollBehavior,
    onClickMovie: (String, upcoming: Boolean) -> Unit,
    state: LazyListState,
    viewModel: ListingViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val currentPromotions by viewModel.currentPromotions.collectAsState()
    val upcomingPromotions by viewModel.upcomingPromotions.collectAsState()
    val currentGroups by viewModel.currentGroups.collectAsState()
    val upcomingGroups by viewModel.upcomingGroups.collectAsState()
    val scope = rememberCoroutineScope()
    ListingScreenContent(
        currentPromotions = currentPromotions,
        upcomingPromotions = upcomingPromotions,
        currentGroups = currentGroups,
        upcomingGroups = upcomingGroups,
        contentPadding = padding,
        behavior = behavior,
        state = state,
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
    ExperimentalPagerApi::class, ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class
)
@Composable
private fun ListingScreenContent(
    currentPromotions: Loadable<List<MovieView>>,
    upcomingPromotions: Loadable<List<MovieView>>,
    currentGroups: Loadable<Map<Genre, List<MovieView>>>,
    upcomingGroups: Loadable<Map<Genre, List<MovieView>>>,
    contentPadding: PaddingValues,
    behavior: TopAppBarScrollBehavior,
    state: LazyListState,
    onClickFavorite: (MovieView) -> Unit,
    onClick: (String, upcoming: Boolean) -> Unit
) {
    val context = LocalContext.current
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(behavior.nestedScrollConnection),
        contentPadding = contentPadding,
        state = state,
    ) {
        item { MoviePromo(items = currentPromotions, onClick = { onClick(it, false) }) }
        currentGroups.onSuccess {
            for ((genre, items) in it) {
                item(key = "current-$genre-title") {
                    SectionHeadline(
                        modifier = Modifier.animateItemPlacement(),
                        name = genre.getName(context)
                    )
                }
                item(key = "current-$genre-content") {
                    MovieRow(
                        modifier = Modifier.animateItemPlacement(),
                        items = Loadable.success(items.immutable()),
                        isShowing = true,
                        onClickFavorite = onClickFavorite,
                        onClick = { onClick(it, false) }
                    )
                }
            }
        }.onLoading {
            item(key = "current-title") {
                SectionHeadline(
                    name = "#".repeat(10),
                    modifier = Modifier
                        .textPlaceholder(true)
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

        // ---

        item {
            SectionTitle(
                modifier = Modifier.animateItemPlacement(),
                name = stringResource(id = R.string.upcoming)
            )
        }
        item {
            MoviePromo(
                modifier = Modifier.animateItemPlacement(),
                items = upcomingPromotions, onClick = { onClick(it, true) })
        }
        upcomingGroups.onSuccess {
            for ((genre, items) in it) {
                item(key = "upcoming-$genre-title") {
                    SectionHeadline(
                        modifier = Modifier.animateItemPlacement(),
                        name = genre.getName(context)
                    )
                }
                item(key = "upcoming-$genre-content") {
                    MovieRow(
                        modifier = Modifier.animateItemPlacement(),
                        items = Loadable.success(items.immutable()),
                        isShowing = false,
                        onClickFavorite = onClickFavorite,
                        onClick = { onClick(it, true) }
                    )
                }
            }
        }.onLoading {
            item(key = "upcoming-title") {
                SectionHeadline(
                    modifier = Modifier
                        .textPlaceholder(true)
                        .animateItemPlacement(),
                    name = "#".repeat(10)
                )
            }
            item(key = "upcoming-content") {
                MovieRow(
                    modifier = Modifier.animateItemPlacement(),
                    items = Loadable.loading(),
                    isShowing = false,
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }.onFailure {
            item(key = "upcoming-content") {
                MovieRow(
                    modifier = Modifier.animateItemPlacement(),
                    items = Loadable.failure(it),
                    isShowing = false,
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }

    }
}

@Composable
fun SectionTitle(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .then(modifier),
        text = name,
        style = Theme.textStyle.title
    )
}

@Composable
fun SectionHeadline(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .then(modifier),
        text = name,
        style = Theme.textStyle.headline
    )
}