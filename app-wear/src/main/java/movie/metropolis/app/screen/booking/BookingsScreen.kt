package movie.metropolis.app.screen.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.AutoCenteringParams
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.ScalingLazyListState
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import movie.metropolis.app.R
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.preview.TicketViewPreviews
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onLoading
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.booking.component.BookingItem
import movie.style.layout.PreviewWearLayout
import movie.style.textPlaceholder
import movie.style.theme.Theme
import androidx.wear.compose.material.Scaffold as WearScaffold

@Composable
fun BookingsScreen(
    viewModel: BookingsViewModel = hiltViewModel(),
    onTicketClick: (String) -> Unit
) {
    val active by viewModel.active.collectAsState()
    val expired by viewModel.expired.collectAsState()
    BookingsScreen(
        active = active,
        expired = expired,
        onTicketClick = onTicketClick
    )
}

@Composable
private fun BookingsScreen(
    active: Loadable<List<TicketView>> = Loadable.loading(),
    expired: Loadable<List<TicketView>> = Loadable.loading(),
    onTicketClick: (String) -> Unit = {},
    state: ScalingLazyListState = rememberScalingLazyListState()
) {
    WearScaffold(
        timeText = {
            TimeText(
                modifier = Modifier.scrollAway(state),
                timeTextStyle = Theme.textStyle.headline
            )
        },
        positionIndicator = {
            PositionIndicator(state)
        }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            autoCentering = AutoCenteringParams(itemOffset = 1),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            item { Text(stringResource(R.string.bookings_title), style = Theme.textStyle.title) }
            active.onSuccess { items ->
                items(items, key = { it.id }) {
                    BookingItem(
                        name = { Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        date = { Text(it.date) },
                        time = { Text(it.time) },
                        onClick = { onTicketClick(it.id) }
                    )
                }
            }.onLoading {
                item {
                    BookingItem(
                        name = { Text("#".repeat(10), modifier = Modifier.textPlaceholder()) },
                        date = { Text("#".repeat(23), modifier = Modifier.textPlaceholder()) },
                        time = { Text("#".repeat(5), modifier = Modifier.textPlaceholder()) },
                        onClick = {}
                    )
                }
            }.onEmpty {
                item {
                    Text(stringResource(R.string.bookings_empty))
                }
            }
            item {
                Text(
                    stringResource(R.string.bookings_expired_title),
                    style = Theme.textStyle.title
                )
            }
            expired.onSuccess { items ->
                items(items, key = { it.id }) {
                    BookingItem(
                        name = { Text(it.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                        date = { Text(it.date) },
                        time = { Text(it.time) },
                        onClick = { onTicketClick(it.id) }
                    )
                }
            }.onLoading {
                item {
                    BookingItem(
                        name = { Text("#".repeat(10), modifier = Modifier.textPlaceholder()) },
                        date = { Text("#".repeat(23), modifier = Modifier.textPlaceholder()) },
                        time = { Text("#".repeat(5), modifier = Modifier.textPlaceholder()) },
                        onClick = {}
                    )
                }
            }.onEmpty {
                item {
                    Text(stringResource(R.string.bookings_empty))
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(TicketViewPreviews::class, 1) previews: List<TicketView>
) = PreviewWearLayout(padding = PaddingValues()) {
    BookingsScreen(Loadable.success(previews))
}