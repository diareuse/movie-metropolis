package movie.metropolis.app.feature.global

import java.util.Date

interface MoviePreview : Movie {
    val screeningFrom: Date
    val media: Iterable<Media>
    val description: String
    val directors: Iterable<String> // link to imdb in the ui
    val cast: Iterable<String> // link to imdb in the ui
    val countryOfOrigin: String
}