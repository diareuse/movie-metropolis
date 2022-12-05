package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NearbyCinemaResponse(
    @SerialName("theatreCode") val cinemaId: String,
    @SerialName("straightLineDistance") val distanceKms: Double
)