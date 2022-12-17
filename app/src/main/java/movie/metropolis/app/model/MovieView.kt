package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface MovieView {
    val id: String
    val name: String
    val releasedAt: String
    val duration: String
    val availableFrom: String
    val directors: List<String>
    val cast: List<String>
    val countryOfOrigin: String
    val favorite: Boolean

    val poster: ImageView?
    val video: VideoView?
}