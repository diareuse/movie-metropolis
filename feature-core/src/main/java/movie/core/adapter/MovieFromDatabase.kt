package movie.core.adapter

import movie.core.db.model.MovieStored
import movie.core.model.Movie
import java.util.Date
import kotlin.time.Duration

data class MovieFromDatabase(
    private val stored: MovieStored
) : Movie {
    override val id: String
        get() = stored.id
    override val name: String
        get() = stored.name
    override val url: String
        get() = stored.url
    override val releasedAt: Date
        get() = stored.releasedAt
    override val screeningFrom: Date
        get() = stored.screeningFrom
    override val duration: Duration
        get() = stored.duration
}