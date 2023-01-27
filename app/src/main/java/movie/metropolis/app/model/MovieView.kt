package movie.metropolis.app.model

import androidx.compose.runtime.Immutable

@Immutable
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
    val rating: String?

    val poster: ImageView?
    val posterLarge: ImageView?
    val video: VideoView?
}