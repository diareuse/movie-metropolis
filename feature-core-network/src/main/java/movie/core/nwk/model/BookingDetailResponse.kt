package movie.core.nwk.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingDetailResponse(
    @SerialName("venueName") val hall: String,
    @SerialName("tickets") val tickets: List<Ticket>
) {

    @Serializable
    data class Ticket(
        @SerialName("row") val row: String,
        @SerialName("seat") val seat: String
    )

}