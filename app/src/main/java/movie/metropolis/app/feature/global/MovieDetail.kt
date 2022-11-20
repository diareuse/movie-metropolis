package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

data class MovieDetail(
    override val id: String,
    override val name: String,
    override val url: String,
    override val releasedAt: Date,
    override val duration: Duration,
    val originalName: String,
    val countryOfOrigin: String,
    val cast: Iterable<String>,
    val directors: Iterable<String>,
    val description: String,
    val screeningFrom: Date,
    val ageRestrictionUrl: String,
    val media: Iterable<Media>
) : Movie