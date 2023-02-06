package movie.metropolis.app.screen.booking

import android.Manifest
import android.annotation.SuppressLint
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import movie.metropolis.app.ActivityActions
import movie.metropolis.app.LocalActivityActions
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onFailure
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.presentation.share.TicketRepresentation
import movie.metropolis.app.screen.booking.component.BookingItemActive
import movie.metropolis.app.screen.booking.component.BookingItemActiveEmpty
import movie.metropolis.app.screen.booking.component.BookingItemExpired
import movie.metropolis.app.screen.booking.component.BookingItemExpiredEmpty
import movie.metropolis.app.screen.booking.component.BookingItemExpiredFailure
import movie.metropolis.app.screen.booking.component.BookingTicketDialog
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.HomeScreenLayout
import movie.metropolis.app.screen.reader.BarcodeReader
import movie.metropolis.app.util.register
import movie.metropolis.app.util.toBitmap
import movie.style.AppButton
import movie.style.AppDialog
import movie.style.AppErrorItem
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    padding: PaddingValues,
    state: LazyListState,
    profileIcon: @Composable () -> Unit,
    onMovieClick: (String) -> Unit,
    viewModel: BookingViewModel = hiltViewModel(),
    actions: ActivityActions = LocalActivityActions.current
) {
    val active by viewModel.active.collectAsState()
    val expired by viewModel.expired.collectAsState()
    val scope = rememberCoroutineScope()
    var isReaderActive by rememberSaveable { mutableStateOf(false) }
    HomeScreenLayout(
        profileIcon = profileIcon,
        title = { Text(stringResource(R.string.tickets)) }
    ) { innerPadding, behavior ->
        BookingScreenContent(
            padding = innerPadding + padding,
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
            onCameraClick = { isReaderActive = true },
            state = state
        )
    }
    ReaderDialog(
        isVisible = isReaderActive,
        onVisibilityChanged = { isReaderActive = it },
        onTicketRead = viewModel::saveTicket
    )
}

@SuppressLint("MissingPermission")
@Composable
private fun ReaderDialog(
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    onTicketRead: (TicketRepresentation) -> Unit,
    actions: ActivityActions = LocalActivityActions.current
) {
    var hasPermission by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (!isVisible) return@LaunchedEffect
        hasPermission = actions.requestPermissions(arrayOf(Manifest.permission.CAMERA))
    }
    AppDialog(
        isVisible = isVisible && hasPermission,
        onVisibilityChanged = onVisibilityChanged
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                shape = Theme.container.card,
                color = Theme.color.container.background,
                shadowElevation = 32.dp
            ) {
                BarcodeReader(
                    modifier = Modifier.fillMaxSize(),
                    format = BarcodeFormat.PDF_417,
                    onBarcodeRead = {
                        onVisibilityChanged(false)
                        onTicketRead(TicketRepresentation.Text(it))
                    }
                )
            }
            val owner = LocalActivityResultRegistryOwner.current?.activityResultRegistry
            val context = LocalContext.current
            val scope = rememberCoroutineScope()
            if (owner != null) AppButton(
                onClick = {
                    scope.launch(Dispatchers.Default) {
                        val image = owner.register("image", GetContent(), "image/*")
                            ?.toBitmap(context)
                            ?.let(TicketRepresentation::Image) ?: return@launch
                        withContext(Dispatchers.Main.immediate) {
                            onTicketRead(image)
                            onVisibilityChanged(false)
                        }
                    }
                },
                elevation = 16.dp
            ) {
                Text(stringResource(R.string.from_file))
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BookingScreenContent(
    padding: PaddingValues,
    active: Loadable<ImmutableList<BookingView.Active>>,
    expired: Loadable<ImmutableList<BookingView.Expired>>,
    behavior: TopAppBarScrollBehavior,
    onRefreshClick: () -> Unit = {},
    onMovieClick: (String) -> Unit = {},
    onShareClick: (BookingView.Active) -> Unit = {},
    onCameraClick: () -> Unit = {},
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = Modifier
            .nestedScroll(behavior.nestedScrollConnection)
            .fillMaxSize(),
        contentPadding = padding + PaddingValues(vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = !active.isLoading || !expired.isLoading,
        state = state
    ) {
        if (!active.isLoading && !expired.isLoading) item("ticket-cta") {
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
        active.onSuccess { view ->
            items(view, BookingView::id) {
                var isVisible by rememberSaveable { mutableStateOf(false) }
                BookingItemActive(
                    modifier = Modifier
                        .animateItemPlacement()
                        .padding(horizontal = 24.dp),
                    name = it.name,
                    cinema = it.cinema.name,
                    date = it.date,
                    time = it.time,
                    poster = it.movie.poster,
                    duration = it.movie.duration,
                    onClick = { isVisible = true },
                    onShare = { onShareClick(it) }
                )
                BookingTicketDialog(
                    code = it.id,
                    poster = it.movie.poster?.url.orEmpty(),
                    hall = it.hall,
                    seats = it.seats.map { it.row to it.seat }.immutable(),
                    time = it.time,
                    name = it.name,
                    isVisible = isVisible,
                    onVisibilityChanged = { isVisible = it }
                )
            }
        }.onLoading {
            item {
                BookingItemActive(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }.onEmpty {
            item {
                BookingItemActiveEmpty(modifier = Modifier.padding(horizontal = 24.dp))
            }
        }.onFailure {
            item {
                AppErrorItem(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    error = stringResource(R.string.error_booking)
                )
            }
        }

        item(key = "divider") { Divider(
            Modifier
                .padding(horizontal = 24.dp)
                .padding(16.dp)) }

        item(key = "expired") {
            MovieItemRow(items = expired, onMovieClick = onMovieClick)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun MovieItemRow(
    items: Loadable<ImmutableList<BookingView.Expired>>,
    onMovieClick: (String) -> Unit
) = LazyRow(
    modifier = Modifier.fillMaxWidth(),
    contentPadding = PaddingValues(horizontal = 24.dp),
    horizontalArrangement = Arrangement.spacedBy(16.dp)
) {
    items.onSuccess { view ->
        items(view, BookingView::id) {
            BookingItemExpired(
                modifier = Modifier.animateItemPlacement(),
                poster = it.movie.poster,
                date = it.date,
                time = it.time,
                name = it.name,
                rating = it.movie.rating,
                onClick = { onMovieClick(it.movie.id) }
            )
        }
    }.onFailure {
        item {
            BookingItemExpiredFailure()
        }
    }.onLoading {
        items(3) {
            BookingItemExpired()
        }
    }.onEmpty {
        item {
            BookingItemExpiredEmpty()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        HomeScreenLayout(
            profileIcon = {},
            title = {}
        ) { padding, behavior ->
            BookingScreenContent(
                padding = padding,
                active = Loadable.loading(),
                expired = Loadable.loading(),
                behavior = behavior
            )
        }
    }
}