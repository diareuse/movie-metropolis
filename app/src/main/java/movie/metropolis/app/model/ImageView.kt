package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface ImageView {
    val aspectRatio: Float
    val url: String
}