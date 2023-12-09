package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView
import movie.rating.MovieMetadata
import movie.style.layout.DefaultPosterAspectRatio

data class MovieViewWithRating(
    private val origin: MovieView,
    private val metadata: MovieMetadata
) : MovieView by origin {

    override val rating: String?
        get() = metadata.rating.takeIf { it > 0 }?.let { "%d%%".format(it) }
    override val poster: ImageView?
        get() = metadata.posterImageUrl.takeUnless { it.isEmpty() }?.let {
            object : ImageView {
                override val aspectRatio: Float
                    get() = DefaultPosterAspectRatio
                override val url: String
                    get() = it
            }
        } ?: origin.poster
    override val url: String
        get() = metadata.url

}