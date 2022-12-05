package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieEventResponse(
    @SerialName("films") val movies: List<MovieResponse>,
    @SerialName("events") val events: List<EventResponse>
)