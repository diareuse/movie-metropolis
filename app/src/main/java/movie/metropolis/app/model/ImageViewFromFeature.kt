package movie.metropolis.app.model

import movie.metropolis.app.feature.global.Media

data class ImageViewFromFeature(
    private val feature: Media.Image
) : ImageView {

    override val aspectRatio: Float
        get() = feature.aspectRatio
    override val url: String
        get() = feature.url

}