package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface ImageView {
    val aspectRatio: Float
    val url: String
}