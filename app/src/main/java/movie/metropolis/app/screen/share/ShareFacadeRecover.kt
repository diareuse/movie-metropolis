package movie.metropolis.app.screen.share

import movie.log.logCatchingResult

class ShareFacadeRecover(
    private val origin: ShareFacade
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation) =
        origin.logCatchingResult("share-ticket") { putTicket(ticket) }

}