package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.VideoView

data class VideoViewFromFeature(
    private val feature: String
) : VideoView {

    override val url: String
        get() = feature
}