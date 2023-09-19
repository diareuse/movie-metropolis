package movie.metropolis.app.screen.cinema

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
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
import movie.metropolis.app.screen.cinema.component.CinemaItemError
import movie.metropolis.app.screen.cinema.component.CinemaItemLoading
import movie.metropolis.app.screen.cinema.component.CinemaViewParameter
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.style.layout.PreviewLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CinemasScreen(
    onClickCinema: (String) -> Unit,
    state: LazyListState,
    profileIcon: @Composable () -> Unit,
    behavior: TopAppBarScrollBehavior,
    viewModel: CinemasViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val location by rememberLocation()
    LaunchedEffect(location) {
        viewModel.location.value = location ?: return@LaunchedEffect
    }
    CinemasScreen(
        items = items,
        behavior = behavior,
        profileIcon = profileIcon,
        onClickCinema = onClickCinema,
        state = state
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun CinemasScreen(
    items: Loadable<List<CinemaView>>,
    behavior: TopAppBarScrollBehavior,
    profileIcon: @Composable () -> Unit,
    onClickCinema: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) = Scaffold(
    topBar = {
        HomeScreenToolbar(
            profileIcon = profileIcon,
            behavior = behavior,
            title = { Text(stringResource(id = R.string.cinemas)) }
        )
    }
) { padding ->
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
                CinemaItemLoading(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }.onSuccess { items ->
            items(items, key = CinemaView::id) {
                CinemaItem(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 24.dp),
                    view = it,
                    onClick = { onClickCinema(it.id) }
                )
            }
        }.onFailure {
            item { CinemaItemError(Modifier.padding(horizontal = 24.dp)) }
        }.onEmpty {
            item { CinemaItemEmpty(Modifier.padding(horizontal = 24.dp)) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun CinemaItemPreview(
    @PreviewParameter(CinemaScreenParameter::class, 4)
    items: Loadable<List<CinemaView>>
) = PreviewLayout(padding = PaddingValues()) {
    CinemasScreen(
        items = items,
        profileIcon = {},
        behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        onClickCinema = {}
    )
}

class CinemaScreenParameter : CollectionPreviewParameterProvider<Loadable<List<CinemaView>>>(
    listOf(
        Loadable.success(CinemaViewParameter().values.toList()),
        Loadable.success(emptyList()),
        Loadable.loading(),
        Loadable.failure(IllegalStateException())
    )
)