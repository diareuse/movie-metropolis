package movie.metropolis.app.screen.cinema

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
import movie.metropolis.app.R
import movie.metropolis.app.feature.shortcut.createShortcut
import movie.metropolis.app.model.CinemaView
import movie.metropolis.app.model.Filter
import movie.metropolis.app.model.MovieBookingView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.cinema.component.MovieShowingItem
import movie.metropolis.app.screen.cinema.component.MovieShowingItemEmpty
import movie.metropolis.app.screen.cinema.component.MovieShowingItemLoading
import movie.metropolis.app.screen.detail.component.FilterRow
import movie.metropolis.app.screen.detail.plus
import movie.style.AppIconButton
import movie.style.AppToolbar
import movie.style.DatePickerRow
import movie.style.state.ImmutableDate
import movie.style.state.ImmutableDate.Companion.immutable
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.textPlaceholder
import movie.style.theme.Theme
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
    val context = LocalContext.current
    CinemaScreen(
        cinema = cinema,
        items = items,
        options = options,
        selectedDate = selectedDate.immutable(),
        onSelectedDateChanged = { viewModel.selectedDate.value = it },
        onBookingClick = onBookingClick,
        onBackClick = onBackClick,
        onFilterClick = viewModel::toggleFilter
    )
    LaunchedEffect(context, cinema) {
        cinema.onSuccess {
            context.createShortcut(it)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun CinemaScreen(
    cinema: Loadable<CinemaView>,
    items: Loadable<List<MovieBookingView>>,
    options: Loadable<Map<Filter.Type, List<Filter>>>,
    selectedDate: ImmutableDate,
    onSelectedDateChanged: (Date) -> Unit,
    onBookingClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onFilterClick: (Filter) -> Unit
) {
    val behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    Scaffold(
        topBar = {
            AppToolbar(
                title = {
                    Text(
                        modifier = Modifier
                            .textPlaceholder(cinema.isLoading),
                        text = cinema.getOrNull()?.name?.substringBefore(',')
                            ?: stringResource(R.string.loading)
                    )
                },
                navigationIcon = {
                    AppIconButton(
                        painter = painterResource(id = R.drawable.ic_back),
                        onClick = onBackClick
                    )
                },
                scrollBehavior = behavior
            )
        }
    ) { padding ->
        var selectedItem by remember { mutableStateOf(null as MovieBookingView?) }
        val columnState = rememberLazyListState()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(behavior.nestedScrollConnection),
            contentPadding = padding + PaddingValues(vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = columnState
        ) {
            item("picker") {
                DatePickerRow(
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
                        text = stringResource(R.string.filters),
                        style = Theme.textStyle.headline
                    )
                }
                item("filters") {
                    Column(
                        modifier = Modifier.animateItemPlacement(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterRow(
                            filters = filters[Filter.Type.Language].orEmpty().immutable(),
                            onFilterToggle = onFilterClick,
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        )
                        FilterRow(
                            filters = filters[Filter.Type.Projection].orEmpty().immutable(),
                            onFilterToggle = onFilterClick,
                            contentPadding = PaddingValues(horizontal = 24.dp)
                        )
                    }
                }
            }
            items.onSuccess { items ->
                items(items, key = { it.movie.id }) {
                    MovieShowingItem(
                        modifier = Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 24.dp),
                        view = it,
                        onClick = onBookingClick
                    )
                }
            }.onLoading {
                items(2) {
                    MovieShowingItemLoading(
                        Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 24.dp)
                    )
                }
            }.onEmpty {
                item {
                    MovieShowingItemEmpty(
                        modifier = Modifier
                            .animateItemPlacement()
                            .padding(horizontal = 24.dp)
                    )
                }
            }
        }
    }
}