package movie.metropolis.app.model.adapter

import androidx.compose.ui.graphics.*
import movie.metropolis.app.model.ImageView

data class ImageViewWithColor(
    private val origin: ImageView,
    override val spotColor: Color
) : ImageView by origin