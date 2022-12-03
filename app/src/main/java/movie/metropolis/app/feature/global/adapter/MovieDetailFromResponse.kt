package movie.metropolis.app.feature.global.adapter

import movie.metropolis.app.feature.global.Media
import movie.metropolis.app.feature.global.MovieDetail
import movie.metropolis.app.feature.global.model.MovieDetailResponse
import java.util.Date
import kotlin.time.Duration

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
        get() = response.duration
    override val originalName: String
        get() = response.nameOriginal
    override val countryOfOrigin: String?
        get() = response.countryOfOrigin
    override val cast: Iterable<String>
        get() = response.cast?.split(", ", ",").orEmpty()
    override val directors: Iterable<String>
        get() = response.directors.split(", ", ",")
    override val description: String
        get() = response.synopsis
    override val screeningFrom: Date
        get() = response.screeningFrom
    override val ageRestrictionUrl: String
        get() = response.restrictionUrl
    override val media: Iterable<Media>
        get() = response.media.mapNotNull(::MediaFromResponse)

    private fun MediaFromResponse(media: MovieDetailResponse.Media) = when (media) {
        is MovieDetailResponse.Media.Image -> Media.Image(media.width, media.height, media.url)
        is MovieDetailResponse.Media.Video -> Media.Video(media.url)
        else -> null
    }

}