package movie.core

import movie.core.model.Booking

interface TicketShareRegistry {

    suspend fun add(ticket: ByteArray)
    suspend fun get(booking: Booking.Active): ByteArray

}

