package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface CinemaView {
    val id: String
    val name: String
    val address: String
    val city: String
    val distance: String?
    val image: String?
}