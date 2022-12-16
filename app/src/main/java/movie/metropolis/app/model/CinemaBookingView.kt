package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface CinemaBookingView : HasAvailability {

    val cinema: CinemaView

}