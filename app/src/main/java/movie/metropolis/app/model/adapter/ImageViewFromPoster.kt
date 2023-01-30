package movie.metropolis.app.model.adapter

import androidx.compose.ui.graphics.Color
import movie.core.model.MoviePromoPoster
import movie.metropolis.app.model.ImageView

data class ImageViewFromPoster(
    private val poster: MoviePromoPoster,
    override val aspectRatio: Float = 1.5f
) : ImageView {

    override val url: String
        get() = poster.url
    override val spotColor: Color
        get() = Color(poster.spotColor)

}