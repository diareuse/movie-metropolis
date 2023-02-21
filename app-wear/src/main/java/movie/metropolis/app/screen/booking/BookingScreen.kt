package movie.metropolis.app.screen.booking

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.preview.TicketViewPreviews
import movie.metropolis.app.presentation.Loadable
import movie.metropolis.app.presentation.onSuccess
import movie.metropolis.app.screen.booking.component.BookingItem
import movie.style.theme.Theme

@Composable
fun BookingScreen(
    viewModel: BookingViewModel = hiltViewModel(),
    onTicketClick: (String) -> Unit
) {
    val items by viewModel.items.collectAsState()
    BookingScreen(
        items = items,
        onTicketClick = onTicketClick
    )
}

@Composable
private fun BookingScreen(
    items: Loadable<List<TicketView>> = Loadable.loading(),
    onTicketClick: (String) -> Unit = {}
) {
    ScalingLazyColumn(modifier = Modifier.fillMaxSize()) {
        items.onSuccess { items ->
            items(items, key = { it.id }) {
                BookingItem(
                    name = { Text(it.name) },
                    date = { Text(it.date) },
                    time = { Text(it.time) },
                    onClick = { onTicketClick(it.id) }
                )
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(TicketViewPreviews::class, 1) previews: List<TicketView>
) = Theme {
    BookingScreen(Loadable.success(previews))
}