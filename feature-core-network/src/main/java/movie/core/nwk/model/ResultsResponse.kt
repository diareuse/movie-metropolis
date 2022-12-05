package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ResultsResponse<T>(
    @SerialName("results") val results: T
)