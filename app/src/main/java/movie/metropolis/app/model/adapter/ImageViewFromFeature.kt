package movie.metropolis.app.model.adapter

import movie.cinema.city.Movie
import movie.metropolis.app.model.ImageView

data class ImageViewFromFeature(
    private val image: Movie.Image
) : ImageView {

    override val aspectRatio: Float
        get() = 1f * image.width / image.height
    override val url: String
        get() = image.url.toString()

}