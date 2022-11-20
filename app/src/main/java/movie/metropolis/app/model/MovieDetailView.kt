package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface MovieDetailView {
    val name: String
    val nameOriginal: String
    val releasedAt: String
    val duration: String
    val countryOfOrigin: String
    val cast: List<String>
    val directors: List<String>
    val description: String
    val availableFrom: String
}