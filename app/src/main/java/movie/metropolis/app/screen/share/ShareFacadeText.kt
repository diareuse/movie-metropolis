package movie.metropolis.app.screen.share

import movie.core.TicketShareRegistry
import movie.log.logCatching

class ShareFacadeText(
    private val share: TicketShareRegistry
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation) =
        share.logCatching("share-text") { add(ticket.bytes) }

}