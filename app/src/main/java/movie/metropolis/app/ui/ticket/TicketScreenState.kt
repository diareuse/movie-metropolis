package movie.metropolis.app.ui.ticket

import androidx.compose.runtime.*
import movie.metropolis.app.model.BookingView
import movie.style.layout.LayoutState

class TicketScreenState {
    val tickets = mutableStateListOf<BookingView>()
    val state by mutableStateOf(LayoutState.loading<Int>())
}