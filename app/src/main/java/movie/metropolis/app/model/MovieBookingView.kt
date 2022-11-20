package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface MovieBookingView {

    val movie: Movie
    val availability: List<Availability>

    interface Movie {
        val id: String
        val name: String
        val releasedAt: String
        val duration: String
        val poster: String
        val video: String
    }

    interface Availability {
        val id: String
        val url: String
        val startsAt: String
        val isEnabled: Boolean
        val cinema: CinemaView
    }

}