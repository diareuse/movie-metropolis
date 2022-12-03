package movie.metropolis.app.feature.global.model.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.global.serializer.TimestampSerializer
import java.util.Date
import kotlin.time.Duration.Companion.hours

@Serializable
internal data class BookingResponse(
    @SerialName("id") val id: String,
    @SerialName("eventName") val name: String,
    @Serializable(TimestampSerializer::class)
    @SerialName("eventDate") val startsAt: Date,
    @Serializable(TimestampSerializer::class)
    @SerialName("transactionDate") val paidAt: Date,
    @SerialName("isPaid") val isPaid: Boolean,
    @SerialName("distributorCode") val movieId: String,
    @SerialName("cinemaId") val cinemaId: String,
    @SerialName("eventId") val eventId: String
) {

    val isExpired
        get() = Date().after(Date(startsAt.time + 3.hours.inWholeMilliseconds))

}