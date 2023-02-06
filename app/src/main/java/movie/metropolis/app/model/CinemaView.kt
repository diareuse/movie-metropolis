package movie.metropolis.app.model

import androidx.compose.runtime.Immutable

@Immutable
interface CinemaView {
    val id: String
    val name: String
    val address: List<String>
    val city: String
    val distance: String?
    val image: String?
}