@file:OptIn(ExperimentalMaterial3Api::class)

package movie.metropolis.app.screen.booking

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
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
import movie.metropolis.app.screen.booking.component.TicketItemExpired
import movie.metropolis.app.screen.home.component.HomeScreenToolbar
import movie.style.AppButton
import movie.style.state.ImmutableList
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    behavior: TopAppBarScrollBehavior,
    onMovieClick: (String) -> Unit,
    contentPadding: PaddingValues,
    viewModel: BookingViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val active by viewModel.active.collectAsState()
    val expired by viewModel.expired.collectAsState()
    val scope = rememberCoroutineScope()
    var isReaderActive by rememberSaveable { mutableStateOf(false) }
    BookingScreenContent(
        active = active,
        expired = expired,
        behavior = behavior,
        onRefreshClick = viewModel::refresh,
        onMovieClick = onMovieClick,
        onShareClick = {
            scope.launch {
                actions.actionShare(viewModel.saveAsFile(it))
            }
        },
        onCameraClick = { isReaderActive = true },
        contentPadding = contentPadding,
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
    active: Loadable<ImmutableList<BookingView>>,
    expired: Loadable<ImmutableList<BookingView>>,
    behavior: TopAppBarScrollBehavior,
    onRefreshClick: () -> Unit = {},
    onMovieClick: (String) -> Unit = {},
    onShareClick: (BookingView) -> Unit = {},
    onCameraClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(),
) = Scaffold(
    topBar = {
        HomeScreenToolbar(
            behavior = behavior,
            title = { Text(stringResource(id = R.string.tickets)) }
        )
    }
) { padding ->
    Column(
        modifier = Modifier
            .padding(padding)
            .padding(contentPadding)
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
                onSuccess = { it },
                onLoading = { emptyList() },
                onFailure = { emptyList() }
            )
            val itemsExpired = expired.fold(
                onSuccess = { it },
                onLoading = { emptyList() },
                onFailure = { emptyList() }
            )
            itemsActive + itemsExpired
        }
        val state = rememberPagerState { items.size }
        HorizontalPager(
            modifier = Modifier.weight(1f),
            state = state,
            contentPadding = PaddingValues(horizontal = 64.dp, vertical = 16.dp),
            pageSpacing = 32.dp
        ) {
            val item = items[it]
            when {
                item.expired -> TicketItemExpired(
                    modifier = Modifier.fillMaxHeight(),
                    item = item,
                    onClick = { onMovieClick(item.movie.id) }
                )

                else -> TicketItemActive(
                    modifier = Modifier.fillMaxHeight(),
                    item = item,
                    onShare = { onShareClick(item) },
                    onClick = { onMovieClick(item.movie.id) }
                )
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun BookingScreenPreview() = Theme {
    BookingScreenContent(
        active = Loadable.loading(),
        expired = Loadable.loading(),
        behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun BookingScreenErrorPreview() = Theme {
    BookingScreenContent(
        active = Loadable.failure(Throwable()),
        expired = Loadable.failure(Throwable()),
        behavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    )
}