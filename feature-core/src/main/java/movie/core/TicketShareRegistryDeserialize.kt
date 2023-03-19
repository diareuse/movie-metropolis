package movie.core

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import movie.core.model.Booking
import movie.core.model.TicketShared

class TicketShareRegistryDeserialize(
    private val store: TicketStore,
    private val json: Json
) : TicketShareRegistry {

    override suspend fun add(ticket: ByteArray) {
        val shared = json.decodeFromString<TicketShared>(ticket.decodeToString())
        store.add(shared)
    }

    override suspend fun get(booking: Booking.Active): ByteArray {
        return json.encodeToString(booking.asTicket()).encodeToByteArray()
    }

    private fun Booking.Active.asTicket() = TicketShared(
        id = id,
        startsAt = startsAt,
        venue = hall,
        movieId = movieId,
        eventId = eventId,
        cinemaId = cinema.id,
        seats = seats.map {
            TicketShared.Seat(
                row = it.row,
                seat = it.seat
            )
        }
    )

}