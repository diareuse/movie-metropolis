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
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.scrollAway
import movie.metropolis.app.R
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.preview.TicketViewPreviews
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onEmpty
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.booking.component.BookingItem
import movie.style.layout.PreviewWearLayout
import movie.style.theme.Theme
import androidx.wear.compose.material.Scaffold as WearScaffold

@Composable
fun BookingsScreen(
    viewModel: BookingsViewModel = hiltViewModel(),
    onTicketClick: (String) -> Unit
) {
    val items by viewModel.items.collectAsState()
    BookingsScreen(
        items = items,
        onTicketClick = onTicketClick
    )
}

@Composable
private fun BookingsScreen(
    items: Loadable<List<TicketView>> = Loadable.loading(),
    onTicketClick: (String) -> Unit = {},
    state: ScalingLazyListState = rememberScalingLazyListState()
) {
    WearScaffold(
        timeText = {
            TimeText(
                modifier = Modifier.scrollAway(state),
                timeTextStyle = Theme.textStyle.headline
            )
        }
    ) {
        ScalingLazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = state,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            autoCentering = AutoCenteringParams(itemOffset = 1)
        ) {
            item { Text(stringResource(R.string.bookings_title), style = Theme.textStyle.title) }
            items.onSuccess { items ->
                items(items, key = { it.id }) {
                    BookingItem(
                        name = { Text(it.name) },
                        date = { Text(it.date) },
                        time = { Text(it.time) },
                        onClick = { onTicketClick(it.id) }
                    )
                }
            }.onEmpty {
                item {
                    Text(
                        text = stringResource(
                            R.string.bookings_empty,
                            stringResource(id = R.string.app_name)
                        ),
                        textAlign = TextAlign.Center
                    )
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