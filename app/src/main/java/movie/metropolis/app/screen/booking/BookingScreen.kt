package movie.metropolis.app.screen.booking

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.input.nestedscroll.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.launch
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.fold
import movie.metropolis.app.screen.booking.component.ReaderDialog
import movie.metropolis.app.screen.booking.component.TicketItemActive
import movie.metropolis.app.screen.booking.component.TicketItemEmpty
import movie.metropolis.app.screen.booking.component.TicketItemError
import movie.metropolis.app.screen.booking.component.TicketItemExpired
import movie.metropolis.app.screen.booking.component.TicketItemLoading
import movie.style.AppButton
import movie.style.state.ImmutableList
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    padding: PaddingValues,
    behavior: TopAppBarScrollBehavior,
    onMovieClick: (String) -> Unit,
    viewModel: BookingViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val active by viewModel.active.collectAsState()
    val expired by viewModel.expired.collectAsState()
    val scope = rememberCoroutineScope()
    var isReaderActive by rememberSaveable { mutableStateOf(false) }
    BookingScreenContent(
        padding = padding,
        behavior = behavior,
        active = active,
        expired = expired,
        onRefreshClick = viewModel::refresh,
        onMovieClick = onMovieClick,
        onShareClick = {
            scope.launch {
                actions.actionShare(viewModel.saveAsFile(it))
            }
        },
        onCameraClick = { isReaderActive = true }
    )
    ReaderDialog(
        isVisible = isReaderActive,
        onVisibilityChanged = { isReaderActive = it },
        onTicketRead = viewModel::saveTicket
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun BookingScreenContent(
    padding: PaddingValues,
    active: Loadable<ImmutableList<BookingView.Active>>,
    expired: Loadable<ImmutableList<BookingView.Expired>>,
    behavior: TopAppBarScrollBehavior,
    onRefreshClick: () -> Unit = {},
    onMovieClick: (String) -> Unit = {},
    onShareClick: (BookingView.Active) -> Unit = {},
    onCameraClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .nestedScroll(behavior.nestedScrollConnection)
            .padding(padding)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!active.isLoading && !expired.isLoading) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.find_new_tickets))
                }
                AppButton(
                    onClick = onCameraClick
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_camera),
                        contentDescription = null
                    )
                }
            }
        }
        val items = remember(active, expired) {
            val itemsActive = active.fold(
                onSuccess = { it.ifEmpty { listOf(BookingView.Empty) } },
                onLoading = { listOf(BookingView.Loading) },
                onFailure = { listOf(BookingView.Error) }
            )
            val itemsExpired = expired.fold(
                onSuccess = { it },
                onLoading = { listOf(BookingView.Loading) },
                onFailure = { listOf(BookingView.Error) }
            )
            itemsActive + itemsExpired
        }
        val state = rememberPagerState { items.size }
        HorizontalPager(
            state = state,
            contentPadding = PaddingValues(horizontal = 64.dp, vertical = 16.dp),
            pageSpacing = 32.dp,
            modifier = Modifier.weight(1f)
        ) {
            when (val item = items[it]) {
                is BookingView.Active -> TicketItemActive(
                    modifier = Modifier.fillMaxHeight(),
                    item = item,
                    onShare = { onShareClick(item) },
                    onClick = { onMovieClick(item.movie.id) }
                )

                is BookingView.Expired -> TicketItemExpired(
                    modifier = Modifier.fillMaxHeight(),
                    item = item,
                    onClick = { onMovieClick(item.movie.id) }
                )

                BookingView.Error -> TicketItemError(
                    modifier = Modifier.fillMaxHeight()
                )

                BookingView.Loading -> TicketItemLoading(
                    modifier = Modifier.fillMaxHeight()
                )

                BookingView.Empty -> TicketItemEmpty(
                    modifier = Modifier.fillMaxHeight()
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        BookingScreenContent(
            padding = PaddingValues(),
            active = Loadable.loading(),
            expired = Loadable.loading(),
            behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
        )
    }
}