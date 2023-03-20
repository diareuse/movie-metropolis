package movie.metropolis.app.model.adapter

import androidx.compose.ui.graphics.*
import movie.metropolis.app.model.ImageView
import movie.metropolis.app.model.MovieView

data class MovieViewWithPoster(
    private val origin: MovieView,
    override val poster: ImageView? = ImageViewAsPoster(origin.poster)
) : MovieView by origin {

    private class ImageViewAsPoster(
        override val aspectRatio: Float = 1.5f,
        override val url: String = "",
        override val spotColor: Color = Color.Black
    ) : ImageView {

        constructor(poster: ImageView?) : this(
            url = poster?.url ?: "",
            spotColor = poster?.spotColor ?: Color.Black
        )

    }

}