package movie.metropolis.app.model

import androidx.compose.runtime.*
import movie.metropolis.app.util.toStringComponents
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Stable
data class MovieView(
    val id: String
) {
    var name: String by mutableStateOf("")
    var releasedAt: String by mutableStateOf("")
    var durationTime: Duration by mutableStateOf(0.seconds)
    val duration: String by derivedStateOf { durationTime.toStringComponents() }
    var availableFrom: String by mutableStateOf("")
    var directors: List<String> by mutableStateOf(emptyList())
    var cast: List<String> by mutableStateOf(emptyList())
    var countryOfOrigin: String by mutableStateOf("")
    var ratingPercent by mutableIntStateOf(0)
    val rating by derivedStateOf { if (ratingPercent > 0) "%d%%".format(ratingPercent) else null }
    var url: String by mutableStateOf("")
    var poster: ImageView? by mutableStateOf(null)
    var posterLarge: ImageView? by mutableStateOf(null)
    var video: VideoView? by mutableStateOf(null)
    var genre: String? by mutableStateOf(null)
}