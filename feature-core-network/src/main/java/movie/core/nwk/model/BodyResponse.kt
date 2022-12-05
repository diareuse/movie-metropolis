package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BodyResponse<T>(
    @SerialName("body") val body: T
)