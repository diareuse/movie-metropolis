package movie.metropolis.app.model

import androidx.compose.runtime.*

@Stable
data class MovieView(
    val id: String
) {
    var name: String by mutableStateOf("")
    var releasedAt: String by mutableStateOf("")
    var duration: String by mutableStateOf("")
    var availableFrom: String by mutableStateOf("")
    var directors: List<String> by mutableStateOf(emptyList())
    var cast: List<String> by mutableStateOf(emptyList())
    var countryOfOrigin: String by mutableStateOf("")
    var rating: String? by mutableStateOf(null)
    var url: String by mutableStateOf("")
    var poster: ImageView? by mutableStateOf(null)
    var posterLarge: ImageView? by mutableStateOf(null)
    var video: VideoView? by mutableStateOf(null)
}