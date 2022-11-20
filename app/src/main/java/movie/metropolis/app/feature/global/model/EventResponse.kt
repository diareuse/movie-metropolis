package movie.metropolis.app.feature.global.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.metropolis.app.feature.global.serializer.LocalTimestampSerializer
import java.util.Date

@Serializable
internal data class EventResponse(
    @SerialName("id") val id: String,
    @SerialName("filmId") val movieId: String,
    @SerialName("cinemaId") val cinemaId: String,
    @Serializable(LocalTimestampSerializer::class)
    @SerialName("eventDateTime") val startsAt: Date,
    @SerialName("bookingLink") val bookingUrl: String,
    @SerialName("attributeIds") val tags: List<String>,
    @SerialName("soldOut") val soldOut: Boolean,
    @SerialName("auditorium") val auditorium: String
)