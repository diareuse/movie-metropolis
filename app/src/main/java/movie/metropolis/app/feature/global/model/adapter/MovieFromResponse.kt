package movie.metropolis.app.feature.global.model.adapter

import movie.metropolis.app.feature.global.model.MovieReference
import movie.metropolis.app.feature.global.model.remote.MovieResponse
import java.util.Date
import kotlin.time.Duration

internal data class MovieFromResponse(
    private val movie: MovieResponse
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
        get() = movie.posterUrl
    override val videoUrl: String?
        get() = movie.videoUrl

}