package movie.metropolis.app.model.adapter

import movie.metropolis.app.feature.global.model.Media
import movie.metropolis.app.model.VideoView

data class VideoViewFromFeature(
    private val feature: Media.Video
) : VideoView {

    override val url: String
        get() = feature.url
}