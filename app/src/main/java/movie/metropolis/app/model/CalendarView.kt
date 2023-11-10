package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface CalendarView {
    val id: String
    val name: String
    val account: String
}