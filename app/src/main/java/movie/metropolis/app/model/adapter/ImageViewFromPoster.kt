package movie.metropolis.app.model.adapter

import movie.core.model.MoviePromoPoster
import movie.metropolis.app.model.ImageView

data class ImageViewFromPoster(
    private val poster: MoviePromoPoster,
    override val aspectRatio: Float = 1.5f
) : ImageView {

    override val url: String
        get() = poster.url

}