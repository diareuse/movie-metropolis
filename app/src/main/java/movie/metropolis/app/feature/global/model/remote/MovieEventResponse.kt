package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class MovieEventResponse(
    @SerialName("films") val movies: List<MovieResponse>,
    @SerialName("events") val events: List<EventResponse>
)