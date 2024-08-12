package movie.cinema.city.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BookingDetailResponse(
    @SerialName("venueName") val hall: String,
    @SerialName("tickets") val tickets: List<Ticket> = emptyList()
) {

    @Serializable
    data class Ticket(
        @SerialName("row") val row: String,
        @SerialName("seat") val seat: String
    )

}