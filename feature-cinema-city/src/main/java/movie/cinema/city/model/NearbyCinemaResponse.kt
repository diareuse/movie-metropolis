package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class NearbyCinemaResponse(
    @SerialName("theatreCode") val cinemaId: String,
    @SerialName("straightLineDistance") val distanceKms: Double
)