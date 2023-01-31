package movie.metropolis.app.screen.listing

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.HomeScreenLayout
import movie.metropolis.app.screen.listing.component.MoviePromo
import movie.metropolis.app.screen.listing.component.MovieRowAlt
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.textPlaceholder
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListingScreen(
    padding: PaddingValues,
    onClickMovie: (String, upcoming: Boolean) -> Unit,
    state: LazyListState,
    profileIcon: @Composable () -> Unit,
    viewModel: ListingAltViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val currentPromotions by viewModel.currentPromotions.collectAsState()
    val upcomingPromotions by viewModel.upcomingPromotions.collectAsState()
    val currentGroups by viewModel.currentGroups.collectAsState()
    val upcomingGroups by viewModel.upcomingGroups.collectAsState()
    val scope = rememberCoroutineScope()
    HomeScreenLayout(
        profileIcon = profileIcon,
        title = { Text(stringResource(R.string.movies)) }
    ) { innerPadding, behavior ->
        ListingScreenContent(
            currentPromotions = currentPromotions,
            upcomingPromotions = upcomingPromotions,
            currentGroups = currentGroups,
            upcomingGroups = upcomingGroups,
            contentPadding = padding + innerPadding,
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
}

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
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
                item(key = "current-$genre-title") { SectionTitle(genre.getName(context)) }
                item(key = "current-$genre-content") {
                    MovieRowAlt(
                        items = Loadable.success(items.immutable()),
                        isShowing = true,
                        onClickFavorite = onClickFavorite,
                        onClick = { onClick(it, false) }
                    )
                }
            }
        }.onLoading {
            item(key = "current-title") {
                SectionTitle(
                    "#".repeat(10),
                    modifier = Modifier.textPlaceholder(true)
                )
            }
            item(key = "current-content") {
                MovieRowAlt(
                    items = Loadable.loading(),
                    isShowing = true,
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }.onFailure {
            item(key = "current-content") {
                MovieRowAlt(
                    items = Loadable.failure(it),
                    isShowing = true,
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }

        // ---

        item { MoviePromo(items = upcomingPromotions, onClick = { onClick(it, true) }) }
        upcomingGroups.onSuccess {
            for ((genre, items) in it) {
                item(key = "upcoming-$genre-title") { SectionTitle(genre.getName(context)) }
                item(key = "upcoming-$genre-content") {
                    MovieRowAlt(
                        items = Loadable.success(items.immutable()),
                        isShowing = false,
                        onClickFavorite = onClickFavorite,
                        onClick = { onClick(it, true) }
                    )
                }
            }
        }.onLoading {
            item(key = "upcoming-title") {
                SectionTitle(
                    "#".repeat(10),
                    modifier = Modifier.textPlaceholder(true)
                )
            }
            item(key = "upcoming-content") {
                MovieRowAlt(
                    items = Loadable.loading(),
                    isShowing = false,
                    onClickFavorite = onClickFavorite,
                    onClick = {}
                )
            }
        }.onFailure {
            item(key = "upcoming-content") {
                MovieRowAlt(
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
        modifier = modifier.padding(horizontal = 24.dp),
        text = name,
        style = Theme.textStyle.title
    )
}