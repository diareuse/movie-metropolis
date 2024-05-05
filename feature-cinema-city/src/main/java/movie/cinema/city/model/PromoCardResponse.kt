package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.cinema.city.serializer.TimestampSerializer
import java.util.Date

@Serializable
internal data class PromoCardResponse(
    @SerialName("@id") val id: String,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("promoImage") val image: String,
    @Serializable(TimestampSerializer::class)
    @SerialName("startDateTime") val start: Date,
    @Serializable(TimestampSerializer::class)
    @SerialName("endDateTime") val end: Date,
    @SerialName("promotionEnabled") val enabled: Boolean,
    @SerialName("pageUrl") val url: String?
)