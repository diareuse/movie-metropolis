package movie.metropolis.app.model

import movie.metropolis.app.feature.global.Media
import movie.metropolis.app.feature.global.MoviePreview
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieViewFromFeature(
    private val movie: MoviePreview
) : MovieView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)

    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name
    override val releasedAt: String
        get() = yearFormat.format(movie.releasedAt)
    override val duration: String
        get() = movie.duration.toStringComponents()
    override val availableFrom: String
        get() = dateFormat.format(movie.screeningFrom)
    override val directors: List<String>
        get() = movie.directors.toList()
    override val cast: List<String>
        get() = movie.cast.toList()
    override val countryOfOrigin: String
        get() = movie.countryOfOrigin
    override val poster: ImageView?
        get() = movie.media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }
            .firstOrNull()
            ?.let(::ImageViewFromFeature)
    override val video: VideoView?
        get() = movie.media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .firstOrNull()
            ?.let(::VideoViewFromFeature)
}