package movie.core.model

import androidx.compose.runtime.*

@Immutable
interface MoviePreview : Movie {
    val media: Iterable<Media>
    val description: String
    val directors: Iterable<String> // link to imdb in the ui
    val cast: Iterable<String> // link to imdb in the ui
    val countryOfOrigin: String
    val genres: Iterable<String>
}