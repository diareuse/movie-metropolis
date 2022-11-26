package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface CinemaBookingView {

    val cinema: CinemaView
    val availability: Map<LanguageAndType, List<Availability>>

    interface Availability {
        val id: String
        val url: String
        val startsAt: String
        val isEnabled: Boolean
    }

    interface LanguageAndType {
        val language: String
        val type: String
    }

}