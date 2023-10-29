package movie.metropolis.app.model.adapter

import movie.core.model.Media
import movie.core.model.MoviePreview
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieViewFromFeature(
    internal val movie: MoviePreview,
    override val favorite: Boolean
) : MovieView {

    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val images
        get() = movie.media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }

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
    override val rating: String?
        get() = null
    override val poster: ImageView?
        get() = images.middleOrNull()?.let { ImageViewFromFeature(it) }
    override val posterLarge: ImageView?
        get() = images.firstOrNull()?.let { ImageViewFromFeature(it) }
    override val video: VideoView?
        get() = movie.media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .firstOrNull()
            ?.let(::VideoViewFromFeature)

    override fun getBase() = movie
}