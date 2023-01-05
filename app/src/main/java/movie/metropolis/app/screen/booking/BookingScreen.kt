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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.zxing.BarcodeFormat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.home.HomeScreenLayout
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.metropolis.app.screen.reader.BarcodeReader
import movie.metropolis.app.screen.share.TicketRepresentation
import movie.metropolis.app.util.register
import movie.metropolis.app.util.toBitmap
import movie.style.AppButton
import movie.style.AppDialog
import movie.style.theme.Theme
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(
    padding: PaddingValues,
    state: LazyListState,
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    profileIcon: @Composable () -> Unit,
    onMovieClick: (String) -> Unit,
    onShareFile: (File) -> Unit,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val active by viewModel.active.collectAsState()
    val expired by viewModel.expired.collectAsState()
    val scope = rememberCoroutineScope()
    var isReaderActive by rememberSaveable { mutableStateOf(false) }
    HomeScreenLayout(
        profileIcon = profileIcon,
        title = { Text("Tickets") }
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
                    onShareFile(viewModel.saveAsFile(it))
                }
            },
            onCameraClick = { isReaderActive = true },
            state = state
        )
    }
    ReaderDialog(
        onPermissionsRequested = onPermissionsRequested,
        isVisible = isReaderActive,
        onVisibilityChanged = { isReaderActive = it },
        onTicketRead = viewModel::saveTicket
    )
}

@SuppressLint("MissingPermission")
@Composable
private fun ReaderDialog(
    onPermissionsRequested: suspend (Array<String>) -> Boolean,
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    onTicketRead: (TicketRepresentation) -> Unit
) {
    var hasPermission by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (!isVisible) return@LaunchedEffect
        hasPermission = onPermissionsRequested(arrayOf(Manifest.permission.CAMERA))
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
                Text("From file")
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun BookingScreenContent(
    padding: PaddingValues,
    active: Loadable<List<BookingView.Active>>,
    expired: Loadable<List<BookingView.Expired>>,
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
        contentPadding = padding + PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = !active.isLoading || !expired.isLoading,
        state = state
    ) {
        if (!active.isLoading && !expired.isLoading) item("ticket-cta") {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                AppButton(
                    onClick = onRefreshClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Find new tickets")
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
                    modifier = Modifier.animateItemPlacement(),
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
                    seats = it.seats.map { it.row to it.seat },
                    time = it.time,
                    name = it.name,
                    isVisible = isVisible,
                    onVisibilityChanged = { isVisible = it }
                )
            }

        }.onLoading {
            items(1) {
                BookingItemActive()
            }
            item(key = "divider") { Divider(Modifier.padding(16.dp)) }
        }

        if (
            active.getOrNull().orEmpty().isNotEmpty() &&
            expired.getOrNull().orEmpty().isNotEmpty()
        ) item(key = "divider") { Divider(Modifier.padding(16.dp)) }

        expired.onSuccess { view ->
            items(view, BookingView::id) {
                BookingItemExpired(
                    modifier = Modifier.animateItemPlacement(),
                    name = it.name,
                    date = it.date,
                    time = it.time,
                    poster = it.movie.poster,
                    duration = it.movie.duration,
                    onClick = { onMovieClick(it.movie.id) }
                )
            }
        }.onLoading {
            items(2) {
                BookingItemExpired()
            }
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