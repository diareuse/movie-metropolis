package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface PersonView {
    val name: String
    val popularity: Int
    val image: String
    val starredInMovies: Int
}