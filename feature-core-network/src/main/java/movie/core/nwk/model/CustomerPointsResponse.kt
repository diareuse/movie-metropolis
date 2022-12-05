package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.nwk.serializer.LocalTimestampSerializer
import java.util.Date

@Serializable
data class CustomerPointsResponse(
    @SerialName("totalPoints") val total: Double,
    @SerialName("pointsToExpire") val expire: Double,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("expirationDate") val expiresAt: Date
)