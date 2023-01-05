package movie.metropolis.app.screen.cinema

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.R
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.DatePickerRow
import movie.metropolis.app.screen.detail.FilterRow
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.style.haptic.withHaptics
import movie.style.textPlaceholder
import java.util.Date

@Composable
fun CinemaScreen(
    viewModel: CinemaViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onBookingClick: (String) -> Unit
) {
    val cinema by viewModel.cinema.collectAsState()
    val items by viewModel.items.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val options by viewModel.options.collectAsState()
    CinemaScreen(
        cinema = cinema,
        items = items,
        options = options,
        selectedDate = selectedDate,
        onSelectedDateChanged = { viewModel.selectedDate.value = it },
        onBookingClick = onBookingClick,
        onBackClick = onBackClick,
        onFilterClick = viewModel::toggleFilter
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CinemaScreen(
    cinema: Loadable<CinemaView>,
    items: Loadable<List<MovieBookingView>>,
    options: Loadable<Map<Filter.Type, List<Filter>>>,
    selectedDate: Date,
    onSelectedDateChanged: (Date) -> Unit,
    onBookingClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onFilterClick: (Filter) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth(.8f)
                            .textPlaceholder(cinema.isLoading),
                        text = cinema.getOrNull()?.name?.substringBefore(',') ?: "Loading…",
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    IconButton(onBackClick.withHaptics()) {
                        Icon(painterResource(id = R.drawable.ic_back), null)
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = padding + PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item("picker") {
                DatePickerRow(
                    start = remember { Date() },
                    selected = selectedDate,
                    onClickDate = onSelectedDateChanged
                )
            }
            options.onSuccess { filters ->
                item("filters-title") {
                    Text(
                        modifier = Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 24.dp),
                        text = "Filters",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                item("filters") {
                    Column(
                        modifier = Modifier.animateItemPlacement(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterRow(
                            filters = filters[Filter.Type.Language].orEmpty(),
                            onFilterToggle = onFilterClick,
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        )
                        FilterRow(
                            filters = filters[Filter.Type.Projection].orEmpty(),
                            onFilterToggle = onFilterClick,
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        )
                    }
                }
                item("filters-divider") {
                    Divider(Modifier.padding(horizontal = 32.dp))
                }
            }
            items.onSuccess { items ->
                items(items, key = { it.movie.id }) {
                    MovieShowingItem(
                        modifier = Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 24.dp),
                        movie = it.movie,
                        availability = it.availability,
                        onClick = onBookingClick
                    )
                }
            }.onLoading {
                items(2) {
                    MovieShowingItem(
                        Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}