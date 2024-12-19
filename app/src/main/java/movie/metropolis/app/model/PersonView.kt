package movie.metropolis.app.model

import androidx.compose.runtime.*
import java.util.Date

@Stable
data class PersonView(val name: String) {
    var url: String by mutableStateOf("")
    var popularity: Int by mutableIntStateOf(0)
    var image: String by mutableStateOf("")
    var movies = mutableStateListOf<Movie>()

    data class Movie(
        val id: Long,
        val name: String,
        val backdrop: String,
        val image: String,
        val popularity: Int,
        val rating: Byte,
        val releasedAt: Date
    )
}