package movie.rating.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.rating.DateSerializer
import java.util.Date

@Serializable
internal data class ActorReferenceResponse(
    @SerialName("id") val id: Long,
    @SerialName("title") val name: String? = null,
    @SerialName("backdrop_path") val backdrop: String? = null,
    @SerialName("poster_path") val image: String? = null,
    @SerialName("popularity") val popularity: Double,
    @SerialName("vote_average") val rating: Double,
    @Serializable(with = DateSerializer::class)
    @SerialName("release_date") val releasedAt: Date? = null
)