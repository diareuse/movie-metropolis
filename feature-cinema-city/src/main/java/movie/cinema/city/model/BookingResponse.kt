package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.cinema.city.serializer.TimestampSerializer
import java.util.Date
import kotlin.time.Duration
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

    fun isExpired(duration: Duration = 3.hours) =
        Date().after(Date(startsAt.time + duration.inWholeMilliseconds))

}