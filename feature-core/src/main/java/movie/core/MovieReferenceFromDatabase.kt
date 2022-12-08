package movie.core

import movie.core.db.model.MovieReferenceView
import movie.core.model.MovieReference
import java.util.Date
import kotlin.time.Duration

data class MovieReferenceFromDatabase(
    private val movie: MovieReferenceView
) : MovieReference {
    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name
    override val url: String
        get() = movie.url
    override val releasedAt: Date
        get() = movie.releasedAt
    override val duration: Duration
        get() = movie.duration
    override val posterUrl: String
        get() = movie.poster
    override val videoUrl: String?
        get() = movie.video
}