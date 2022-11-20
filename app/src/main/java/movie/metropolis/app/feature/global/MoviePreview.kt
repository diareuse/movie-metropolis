package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

data class MoviePreview(
    override val id: String,
    override val name: String,
    override val url: String,
    override val releasedAt: Date,
    override val duration: Duration,

    val screeningFrom: Date,
    val media: Iterable<Media>,
    val description: String,
    val directors: Iterable<String>, // link to imdb in the ui
    val cast: Iterable<String>, // link to imdb in the ui
    val countryOfOrigin: String
) : Movie