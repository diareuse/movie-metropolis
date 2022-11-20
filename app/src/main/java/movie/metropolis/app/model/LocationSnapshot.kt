package movie.metropolis.app.model

import androidx.compose.runtime.Stable

@Stable
interface LocationSnapshot {
    val latitude: Double
    val longitude: Double
}