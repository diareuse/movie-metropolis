package movie.metropolis.app.screen.share

import movie.core.TicketShareRegistry
import movie.log.logSevere

class ShareFacadeText(
    private val share: TicketShareRegistry
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation) =
        share.runCatching { add(ticket.bytes) }.logSevere()

}