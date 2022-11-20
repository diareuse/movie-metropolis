package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface BookingView {
    val id: String
    val name: String
    val date: String
    val time: String
    val isExpired: Boolean
    val isPaid: Boolean
}