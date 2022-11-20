package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

data class Movie(
    val id: String,
    val name: String,
    val originalName: String,
    val duration: Duration,
    val url: String,
    val releasedAt: Date,
    val countryOfOrigin: String,
    val cast: Iterable<String>,
    val directors: Iterable<String>,
    val description: String,
    val screeningFrom: Date,
    val ageRestrictionUrl: String,
    val media: Iterable<Media>
)