package movie.metropolis.app.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
interface ImageView {
    val aspectRatio: Float
    val url: String
    val spotColor: Color
}