package movie.metropolis.app.model

import androidx.compose.runtime.*

@Immutable
interface LocationSnapshot {
    val latitude: Double
    val longitude: Double

    companion object {
        operator fun invoke(latitude: Double, longitude: Double): LocationSnapshot {
            return LocationSnapshotImpl(latitude, longitude)
        }
    }
}

private data class LocationSnapshotImpl(
    override val latitude: Double,
    override val longitude: Double
) : LocationSnapshot