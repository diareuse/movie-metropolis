package movie.rating.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListResponse<T>(
    @SerialName("results")
    val results: List<T>
)