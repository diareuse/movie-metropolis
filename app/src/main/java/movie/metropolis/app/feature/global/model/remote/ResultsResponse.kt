package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ResultsResponse<T>(
    @SerialName("results") val results: T
)