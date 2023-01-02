package movie.core.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import movie.core.adapter.DateAsSecondsSerializer
import java.util.Date

@Serializable
data class TicketShared(
    @SerialName("id")
    override val id: String,
    @Serializable(DateAsSecondsSerializer::class)
    @SerialName("sa")
    override val startsAt: Date,
    @SerialName("v")
    override val venue: String,
    @SerialName("mid")
    override val movieId: String,
    @SerialName("eid")
    override val eventId: String,
    @SerialName("cid")
    override val cinemaId: String,
    @SerialName("s")
    override val seats: List<Seat>
) : Ticket {

    @Serializable
    data class Seat(
        @SerialName("r")
        override val row: String,
        @SerialName("s")
        override val seat: String
    ) : Ticket.Seat

}