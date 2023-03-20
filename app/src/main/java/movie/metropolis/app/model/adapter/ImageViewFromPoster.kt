package movie.metropolis.app.model.adapter

import androidx.compose.ui.graphics.*
import movie.core.model.MoviePromoPoster
import movie.metropolis.app.model.ImageView

data class ImageViewFromPoster(
    private val poster: MoviePromoPoster,
    override val spotColor: Color,
    override val aspectRatio: Float = 1.5f
) : ImageView {

    override val url: String
        get() = poster.url

}