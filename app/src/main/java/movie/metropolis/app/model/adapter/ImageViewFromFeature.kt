package movie.metropolis.app.model.adapter

import movie.core.model.Media
import movie.metropolis.app.model.ImageView

data class ImageViewFromFeature(
    private val feature: Media.Image
) : ImageView {

    override val aspectRatio: Float
        get() = feature.aspectRatio
    override val url: String
        get() = feature.url

}