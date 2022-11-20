package movie.metropolis.app.feature.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.user.serializer.LocalTimestampSerializer
import java.util.Date

@Serializable
internal data class CustomerPointsResponse(
    @SerialName("totalPoints") val total: Double,
    @SerialName("pointsToExpire") val expire: Double,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("expirationDate") val expiresAt: Date
)