package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.cinema.component.CinemaItem
import movie.metropolis.app.screen.cinema.component.CinemaItemEmpty
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.HomeScreenState
import movie.style.AppErrorItem
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemasScreen(
    padding: PaddingValues,
    onClickCinema: (String) -> Unit,
    state: LazyListState,
    behavior: TopAppBarScrollBehavior,
    homeState: HomeScreenState,
    viewModel: CinemasViewModel = hiltViewModel()
) {
    SideEffect {
        homeState.title = R.string.cinemas
    }
    val items by viewModel.items.collectAsState()
    val location by rememberLocation()
    LaunchedEffect(location) {
        viewModel.location.value = location ?: return@LaunchedEffect
    }
    CinemasScreen(
        items = items,
        behavior = behavior,
        padding = padding,
        onClickCinema = onClickCinema,
        state = state
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CinemasScreen(
    items: Loadable<List<CinemaView>>,
    padding: PaddingValues,
    behavior: TopAppBarScrollBehavior,
    onClickCinema: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .nestedScroll(behavior.nestedScrollConnection)
            .fillMaxSize(),
        contentPadding = padding + PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = state,
        userScrollEnabled = items.isSuccess
    ) {
        items.onLoading {
            items(7) {
                CinemaItem(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }.onSuccess { items ->
            items(items, key = CinemaView::id) {
                CinemaItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 24.dp),
                    name = it.name,
                    address = it.address.immutable(),
                    city = it.city,
                    distance = it.distance,
                    image = it.image,
                    onClick = { onClickCinema(it.id) }
                )
            }
        }.onFailure {
            item {
                AppErrorItem(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    error = stringResource(R.string.error_cinemas)
                )
            }
        }.onEmpty {
            item {
                CinemaItemEmpty(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        CinemasScreen(
            items = Loadable.loading(),
            padding = PaddingValues(),
            behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            onClickCinema = {}
        )
    }
}