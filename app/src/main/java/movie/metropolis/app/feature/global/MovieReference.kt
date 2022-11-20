package movie.metropolis.app.feature.global

import java.util.Date
import kotlin.time.Duration

data class MovieReference(
    override val id: String,
    override val name: String,
    override val url: String,
    override val releasedAt: Date,
    override val duration: Duration,

    val posterUrl: String,
    val videoUrl: String
) : Movie