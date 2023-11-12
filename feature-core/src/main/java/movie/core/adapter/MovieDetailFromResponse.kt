package movie.core.adapter

import movie.core.model.Media
import movie.core.model.MovieDetail
import movie.core.nwk.model.MovieDetailResponse
import java.util.Date
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

internal data class MovieDetailFromResponse(
    private val response: MovieDetailResponse
) : MovieDetail {

    override val id: String
        get() = response.id
    override val name: String
        get() = response.name
    override val url: String
        get() = response.url
    override val releasedAt: Date
        get() = response.releasedAt
    override val duration: Duration
        get() = response.duration ?: 0.seconds
    override val originalName: String
        get() = response.nameOriginal
    override val countryOfOrigin: String?
        get() = response.countryOfOrigin
    override val cast: Iterable<String>
        get() = response.cast?.split(", ", ",").orEmpty()
    override val directors: Iterable<String>
        get() = response.directors.orEmpty().split(", ", ",")
    override val description: String
        get() = response.synopsis.orEmpty()
    override val screeningFrom: Date
        get() = response.screeningFrom
    override val ageRestrictionUrl: String
        get() = response.restrictionUrl
    override val media: Iterable<Media>
        get() = response.media.mapNotNull(::MediaFromResponse)

    private fun MediaFromResponse(media: MovieDetailResponse.Media) =
        when (media) {
            is MovieDetailResponse.Media.Image -> Media.Image(
                media.width,
                media.height,
                media.url
            )

            is MovieDetailResponse.Media.Video -> Media.Video(media.url)
            else -> null
        }

}