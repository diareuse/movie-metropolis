package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

data class MovieReference(
    val distributorCode: String, // is id
    val name: String,
    val duration: Duration,
    val posterUrl: String,
    val videoUrl: String,
    val url: String,
    val releasedAt: Date
)