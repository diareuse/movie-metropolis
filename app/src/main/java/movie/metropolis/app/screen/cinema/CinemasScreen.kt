package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.feature.location.rememberLocation
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.theme.Theme

@Composable
fun CinemasScreen(
    padding: PaddingValues,
    onPermissionRequested: suspend (Array<String>) -> Boolean,
    onClickCinema: (String) -> Unit,
    state: LazyListState,
    viewModel: CinemasViewModel = hiltViewModel()
) {
    val items by viewModel.items.collectAsState()
    val location by rememberLocation(onPermissionRequested)
    SideEffect {
        viewModel.location.value = location
    }
    CinemasScreen(
        items = items,
        padding = padding,
        onClickCinema = onClickCinema,
        state = state
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CinemasScreen(
    items: Loadable<List<CinemaView>>,
    padding: PaddingValues,
    onClickCinema: (String) -> Unit,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = padding + PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        state = state
    ) {
        if (items.isLoading) items(4) {
            CinemaItem(modifier = Modifier.padding(horizontal = 24.dp))
        }
        items(items.getOrNull().orEmpty(), key = CinemaView::id) {
            CinemaItem(
                modifier = Modifier
                    .animateItemPlacement()
                    .padding(horizontal = 24.dp),
                name = it.name,
                address = it.address,
                city = it.city,
                distance = it.distance,
                onClick = { onClickCinema(it.id) }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        CinemasScreen(
            items = Loadable.loading(),
            padding = PaddingValues(0.dp),
            onClickCinema = {}

        )
    }
}