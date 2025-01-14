package movie.metropolis.app.ui.ticket

import androidx.compose.runtime.*
import movie.metropolis.app.model.BookingView

class TicketScreenState {
    val tickets = mutableStateListOf<BookingView>()
}