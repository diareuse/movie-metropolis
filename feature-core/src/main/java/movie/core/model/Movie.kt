package movie.core.model

import java.util.Date
import kotlin.time.Duration

interface Movie {
    val id: String
    val name: String
    val url: String
    val releasedAt: Date
    val screeningFrom: Date
    val duration: Duration
}