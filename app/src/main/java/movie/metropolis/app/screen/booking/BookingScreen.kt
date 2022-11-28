package movie.metropolis.app.screen.booking

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import movie.metropolis.app.model.BookingView
import movie.metropolis.app.screen.Loadable
import movie.metropolis.app.screen.detail.plus
import movie.metropolis.app.screen.onLoading
import movie.metropolis.app.screen.onSuccess
import movie.metropolis.app.theme.Theme

@Composable
fun BookingScreen(
    padding: PaddingValues,
    viewModel: BookingViewModel = hiltViewModel()
) {
    val active by viewModel.active.collectAsState()
    val expired by viewModel.expired.collectAsState()
    BookingScreen(
        padding = padding,
        active = active,
        expired = expired
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun BookingScreen(
    padding: PaddingValues,
    active: Loadable<List<BookingView.Active>>,
    expired: Loadable<List<BookingView.Expired>>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = padding + PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        userScrollEnabled = !active.isLoading || !expired.isLoading
    ) {
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
                    onClick = { isVisible = true }
                )
                BookingTicketDialog(
                    code = it.id,
                    poster = it.movie.poster?.url.orEmpty(),
                    hall = it.hall,
                    seats = it.seats.map { it.row to it.seat },
                    time = it.time,
                    name = it.name,
                    isVisible = isVisible,
                    onDismissRequest = { isVisible = false }
                )
            }
            item(key = "divider") { Divider(Modifier.padding(16.dp)) }
        }.onLoading {
            items(1) {
                BookingItemActive()
            }
            item(key = "divider") { Divider(Modifier.padding(16.dp)) }
        }
        expired.onSuccess { view ->
            items(view, BookingView::id) {
                BookingItemExpired(
                    modifier = Modifier.animateItemPlacement(),
                    name = it.name,
                    date = it.date,
                    time = it.time,
                    poster = it.movie.poster,
                    duration = it.movie.duration
                )
            }
        }.onLoading {
            items(2) {
                BookingItemExpired()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        BookingScreen(
            padding = PaddingValues(0.dp),
            active = Loadable.loading(),
            expired = Loadable.loading()
        )
    }
}