package movie.metropolis.app.model

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
interface ImageView {
    val aspectRatio: Float
    val url: String
    val spotColor: Color
}