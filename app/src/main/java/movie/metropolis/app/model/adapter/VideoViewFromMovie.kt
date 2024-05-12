package movie.metropolis.app.model.adapter

import movie.metropolis.app.model.VideoView
import java.net.URL

data class VideoViewFromMovie(
    private val video: URL
) : VideoView {
    override val url: String
        get() = video.toString()
}