package movie.metropolis.app.feature.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.user.serializer.TimestampSerializer
import java.util.Date

@Serializable
internal data class BookingResponse(
    @SerialName("id") val id: String,
    @SerialName("eventName") val name: String,
    @Serializable(TimestampSerializer::class)
    @SerialName("eventDate") val startsAt: Date,
    @Serializable(TimestampSerializer::class)
    @SerialName("transactionDate") val paidAt: Date,
    @SerialName("isPaid") val isPaid: Boolean,
    @SerialName("eventMasterCode") val eventMasterCode: String,
    @SerialName("cinemaId") val cinemaId: String,
    @SerialName("eventId") val eventId: String
)