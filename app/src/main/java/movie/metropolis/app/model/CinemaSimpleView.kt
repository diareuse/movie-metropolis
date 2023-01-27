package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface CinemaSimpleView {
    val id: String
    val name: String
    val city: String
}