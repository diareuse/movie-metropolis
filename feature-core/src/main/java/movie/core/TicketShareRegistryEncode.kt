package movie.core

import movie.core.model.Booking

class TicketShareRegistryEncode(
    private val origin: TicketShareRegistry
) : TicketShareRegistry {

    override suspend fun add(ticket: ByteArray) {
        origin.add(ticket.base91Decode())
    }

    override suspend fun get(booking: Booking): ByteArray {
        return origin.get(booking).base91Encode()
    }

}