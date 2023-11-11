package movie.rating.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ActorResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("popularity") val popularity: Double,
    @SerialName("profile_path") val picture: String? = null,
    @SerialName("known_for") val references: List<ActorReferenceResponse>
)