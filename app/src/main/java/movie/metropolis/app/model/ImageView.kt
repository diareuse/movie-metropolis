package movie.metropolis.app.model

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*

@Immutable
interface ImageView {
    val aspectRatio: Float
    val url: String
    val spotColor: Color
}