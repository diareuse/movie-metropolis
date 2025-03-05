package movie.metropolis.app.model

import androidx.compose.runtime.*

@Stable
data class MovieDetailView(
    val id: String
) {
    var name: String by mutableStateOf("")
    var nameOriginal: String by mutableStateOf("")
    var releasedAt: String by mutableStateOf("")
    var duration: String by mutableStateOf("")
    var countryOfOrigin: String by mutableStateOf("")
    var cast: List<PersonView> by mutableStateOf(emptyList())
    var directors: List<PersonView> by mutableStateOf(emptyList())
    var description: String by mutableStateOf("")
    var availableFrom: String by mutableStateOf("")
    var poster: ImageView? by mutableStateOf(null)
    var backdrop: ImageView? by mutableStateOf(null)
    var trailer: VideoView? by mutableStateOf(null)
    val rating: String? by derivedStateOf {
        if (ratingNumber > 0) "${
            ratingNumber.times(100).toInt()
        }%" else null
    }
    var ratingNumber: Float by mutableFloatStateOf(-1f)
    var url: String by mutableStateOf("")
}