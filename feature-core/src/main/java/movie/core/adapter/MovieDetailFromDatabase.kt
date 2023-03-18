package movie.core.adapter

import movie.core.db.model.MovieDetailView
import movie.core.db.model.MovieMediaView
import movie.core.model.Media
import movie.core.model.MovieDetail
import java.util.Date
import kotlin.time.Duration

data class MovieDetailFromDatabase(
    private val movie: MovieDetailView,
    private val mediaStored: List<MovieMediaView>
) : MovieDetail {
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
    override val originalName: String
        get() = movie.originalName
    override val countryOfOrigin: String?
        get() = movie.countryOfOrigin
    override val cast: Iterable<String>
        get() = movie.cast
    override val directors: Iterable<String>
        get() = movie.directors
    override val description: String
        get() = movie.description
    override val screeningFrom: Date
        get() = movie.screeningFrom
    override val ageRestrictionUrl: String
        get() = movie.ageRestrictionUrl
    override val media: Iterable<Media>
        get() = mediaStored.map(::MediaFromDatabase)
    override val rating: Byte
        get() = movie.rating
    override val linkImdb: String?
        get() = movie.linkImdb
    override val linkRottenTomatoes: String?
        get() = movie.linkRottenTomatoes
    override val linkCsfd: String?
        get() = movie.linkCsfd
}