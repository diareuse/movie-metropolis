package movie.core.model

import androidx.compose.runtime.*
import java.util.Date

@Immutable
interface MoviePreview : Movie {
    val screeningFrom: Date
    val media: Iterable<Media>
    val description: String
    val directors: Iterable<String> // link to imdb in the ui
    val cast: Iterable<String> // link to imdb in the ui
    val countryOfOrigin: String
    val genres: Iterable<String>
}