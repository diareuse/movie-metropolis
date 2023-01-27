package movie.metropolis.app.model

import androidx.compose.runtime.Immutable

@Immutable
interface CinemaSimpleView {
    val id: String
    val name: String
    val city: String
}