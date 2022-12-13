package movie.core.adapter

import movie.core.model.Media
import movie.core.model.MoviePreview
import java.util.Date
import java.util.Locale
import kotlin.time.Duration

internal data class MoviePreviewFromResponse(
    private val response: movie.core.nwk.model.ExtendedMovieResponse
) : MoviePreview {

    private val metadata = response.metadata[Locale.getDefault()]
        ?: response.metadata[Locale("en", "GB")]
        ?: response.metadata.entries.first().value

    override val id: String
        get() = response.id.key
    override val name: String
        get() = metadata.name
    override val url: String
        get() = response.url
    override val releasedAt: Date
        get() = response.releasedAt
    override val duration: Duration
        get() = response.duration
    override val screeningFrom: Date
        get() = response.screeningFrom
    override val media: Iterable<Media>
        get() = response.media.mapNotNull(::MediaFromResponse)
    override val description: String
        get() = metadata.synopsis.orEmpty()
    override val directors: Iterable<String>
        get() = metadata.directors.split(", ", ",")
    override val cast: Iterable<String>
        get() = metadata.cast?.split(", ", ",").orEmpty()
    override val countryOfOrigin: String
        get() = metadata.countryOfOrigin.orEmpty()

    private fun MediaFromResponse(media: movie.core.nwk.model.ExtendedMovieResponse.Media) =
        when (media) {
            is movie.core.nwk.model.ExtendedMovieResponse.Media.Image -> Media.Image(
                media.width,
                media.height,
                media.url
            )

            is movie.core.nwk.model.ExtendedMovieResponse.Media.Video -> Media.Video(media.url)
            else -> null
        }
}