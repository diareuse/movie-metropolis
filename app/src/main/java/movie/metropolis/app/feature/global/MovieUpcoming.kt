package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

data class MovieUpcoming(
    val id: String,
    val url: String,
    val releasedAt: Date,
    val screeningFrom: Date,
    val duration: Duration,
    val distributorCode: String,
    val media: Iterable<Media>,
    val name: String,
    val description: String,
    val directors: Iterable<String>, // link to imdb in the ui
    val cast: Iterable<String>, // link to imdb in the ui
    val countryOfOrigin: String
)