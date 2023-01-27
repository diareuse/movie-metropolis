package movie.metropolis.app.model

import androidx.compose.runtime.Immutable

@Immutable
interface MovieBookingView : HasAvailability {

    val movie: Movie

    @Immutable
    interface Movie {
        val id: String
        val name: String
        val releasedAt: String
        val duration: String
        val poster: String
        val video: String?
    }

}