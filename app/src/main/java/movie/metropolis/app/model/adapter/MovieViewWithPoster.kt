package movie.metropolis.app.model.adapter

import movie.core.model.MoviePromoPoster
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView

data class MovieViewWithPoster(
    private val origin: MovieView,
    override val poster: ImageView? = null
) : MovieView by origin {

    constructor(
        origin: MovieView,
        poster: MoviePromoPoster
    ) : this(
        origin = origin,
        poster = ImageViewFromPoster(poster)
    )

}