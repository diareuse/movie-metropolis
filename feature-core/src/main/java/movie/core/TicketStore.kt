package movie.core

import movie.core.model.Ticket

class TicketStore {

    private val values = mutableSetOf<Ticket>()

    fun add(ticket: Ticket) {
        values += ticket
    }

    fun getAll() = values.toList().also {
        values.clear()
    }

}