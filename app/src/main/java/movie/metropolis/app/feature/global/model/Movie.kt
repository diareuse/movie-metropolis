package movie.metropolis.app.feature.global.model

import java.util.Date
import kotlin.time.Duration

interface Movie {
    val id: String
    val name: String
    val url: String
    val releasedAt: Date
    val duration: Duration
}