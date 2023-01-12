package movie.core.adapter

import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.core.model.MoviePreview
import java.util.Date
import kotlin.time.Duration

class MoviePreviewFromDetail(
    private val detail: MovieDetail
) : MoviePreview {

    override val id: String
        get() = detail.id
    override val name: String
        get() = detail.name
    override val url: String
        get() = detail.url
    override val releasedAt: Date
        get() = detail.releasedAt
    override val duration: Duration
        get() = detail.duration
    override val screeningFrom: Date
        get() = detail.screeningFrom
    override val media: Iterable<Media>
        get() = detail.media
    override val description: String
        get() = detail.description
    override val directors: Iterable<String>
        get() = detail.directors
    override val cast: Iterable<String>
        get() = detail.cast
    override val countryOfOrigin: String
        get() = detail.countryOfOrigin.orEmpty()
    override val spotColor: Int
        get() = detail.spotColor

}