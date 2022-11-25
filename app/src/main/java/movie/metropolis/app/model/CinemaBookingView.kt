package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface CinemaBookingView {

    val cinema: CinemaView
    val availability: Map<String, List<Availability>>

    interface Availability {
        val id: String
        val url: String
        val startsAt: String
        val isEnabled: Boolean
    }

}