package movie.metropolis.app.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color

@Stable
@Immutable
interface ImageView {
    val aspectRatio: Float
    val url: String
    val spotColor: Color
}