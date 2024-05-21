package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.metropolis.app.model.VideoView

data class MovieViewFromMovie(
    private val movie: Movie
) : MovieView {
    override val id: String
        get() = movie.id
    override val name: String
        get() = movie.name.localized
    override val releasedAt: String
        get() = movie.releasedAt.toString()
    override val duration: String
        get() = movie.length?.toString().orEmpty()
    override val availableFrom: String
        get() = movie.screeningFrom.toString()
    override val directors: List<String>
        get() = movie.directors
    override val cast: List<String>
        get() = movie.cast
    override val countryOfOrigin: String
        get() = movie.originCountry.orEmpty()
    override val rating: String?
        get() = null
    override val url: String
        get() = movie.link.toString()
    override val poster: ImageView?
        get() = movie.images.minByOrNull { it.width * it.height }?.let(::ImageViewFromMovie)
    override val posterLarge: ImageView?
        get() = movie.images.maxByOrNull { it.width * it.height }?.let(::ImageViewFromMovie)
    override val video: VideoView?
        get() = movie.videos.firstOrNull()?.let(::VideoViewFromMovie)
}