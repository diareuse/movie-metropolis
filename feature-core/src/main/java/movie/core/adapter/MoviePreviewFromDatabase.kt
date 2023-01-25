package movie.core.adapter

import movie.core.db.model.MovieMediaView
import movie.core.db.model.MoviePreviewView
import movie.core.model.Media
import movie.core.model.MoviePreview
import java.util.Date
import kotlin.time.Duration

data class MoviePreviewFromDatabase(
    private val movie: MoviePreviewView,
    private val mediaStored: List<MovieMediaView>
) : MoviePreview {
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
    override val screeningFrom: Date
        get() = movie.screeningFrom
    override val media: Iterable<Media>
        get() = mediaStored.map(::MediaFromDatabase)
    override val description: String
        get() = movie.description
    override val directors: Iterable<String>
        get() = movie.directors
    override val cast: Iterable<String>
        get() = movie.cast
    override val countryOfOrigin: String
        get() = movie.countryOfOrigin
    override val spotColor: Int
        get() = 0xff000000.toInt()
    override val rating: Byte?
        get() = null
    override val genres: Iterable<String>
        get() = movie.genres
}