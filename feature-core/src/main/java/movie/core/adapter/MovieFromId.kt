package movie.core.adapter

import movie.core.model.Movie
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

data class MovieFromId(
    override val id: String
) : Movie {

    override val name: String = ""
    override val url: String = ""
    override val releasedAt: Date = Date(0)
    override val duration: Duration = 0.seconds

}