package movie.metropolis.app.feature.global.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BodyResponse<T>(
    @SerialName("body") val body: T
)