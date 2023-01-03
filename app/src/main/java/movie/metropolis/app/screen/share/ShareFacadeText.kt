package movie.metropolis.app.screen.share

import movie.core.TicketShareRegistry

class ShareFacadeText(
    private val share: TicketShareRegistry
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation): Result<Unit> {
        return share.runCatching { add(ticket.bytes) }
    }

}