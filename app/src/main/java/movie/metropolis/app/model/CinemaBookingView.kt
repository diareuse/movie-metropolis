package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface CinemaBookingView : HasAvailability {

    val cinema: CinemaView

}