package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResultsResponse<T>(
    @SerialName("results") val results: T
)