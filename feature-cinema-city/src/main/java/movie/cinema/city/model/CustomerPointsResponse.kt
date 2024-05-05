package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.cinema.city.serializer.LocalTimestampSerializer
import java.util.Date

@Serializable
internal data class CustomerPointsResponse(
    @SerialName("totalPoints") val total: Double,
    @SerialName("pointsToExpire") val expire: Double,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("expirationDate") val expiresAt: Date
)