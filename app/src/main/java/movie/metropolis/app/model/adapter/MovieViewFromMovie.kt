package movie.metropolis.app.model.adapter

import movie.core.model.Media
import movie.core.model.Movie
import movie.core.model.MovieFavorite
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView
import movie.metropolis.app.util.toStringComponents
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale

data class MovieViewFromMovie(
    private val movie: MovieFavorite,
    private val media: Iterable<Media>
) : MovieView {
    private val yearFormat = SimpleDateFormat("yyyy", Locale.getDefault())
    private val dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM)
    private val images
        get() = media
            .asSequence()
            .filterIsInstance<Media.Image>()
            .sortedByDescending { it.height * it.width }

    override val id: String
        get() = movie.movie.id
    override val name: String
        get() = movie.movie.name
    override val releasedAt: String
        get() = yearFormat.format(movie.movie.releasedAt)
    override val duration: String
        get() = movie.movie.duration.toStringComponents()
    override val availableFrom: String
        get() = dateFormat.format(movie.movie.screeningFrom)
    override val directors: List<String>
        get() = emptyList()
    override val cast: List<String>
        get() = emptyList()
    override val countryOfOrigin: String
        get() = ""
    override val favorite: Boolean
        get() = true
    override val rating: String?
        get() = null
    override val poster: ImageView?
        get() = images.middleOrNull()?.let { ImageViewFromFeature(it) }
    override val posterLarge: ImageView?
        get() = images.firstOrNull()?.let { ImageViewFromFeature(it) }
    override val video: VideoView?
        get() = media
            .asSequence()
            .filterIsInstance<Media.Video>()
            .firstOrNull()
            ?.let(::VideoViewFromFeature)

    override fun getBase(): Movie {
        return movie.movie
    }
}