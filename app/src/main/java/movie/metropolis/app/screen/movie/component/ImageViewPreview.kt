package movie.metropolis.app.screen.movie.component

import movie.metropolis.app.model.ImageView
import movie.style.layout.DefaultPosterAspectRatio

data class ImageViewPreview(
    override val url: String = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
    override val aspectRatio: Float = DefaultPosterAspectRatio
) : ImageView