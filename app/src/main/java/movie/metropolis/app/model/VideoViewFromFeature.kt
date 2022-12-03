package movie.metropolis.app.model

import movie.metropolis.app.feature.global.Media

data class VideoViewFromFeature(
    private val feature: Media.Video
) : VideoView {

    override val url: String
        get() = feature.url
}