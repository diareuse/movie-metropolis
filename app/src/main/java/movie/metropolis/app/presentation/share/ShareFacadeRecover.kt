package movie.metropolis.app.presentation.share

import movie.log.flatMapCatching
import movie.log.logSevere

class ShareFacadeRecover(
    private val origin: ShareFacade
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation) =
        origin.flatMapCatching { putTicket(ticket) }.logSevere()

}