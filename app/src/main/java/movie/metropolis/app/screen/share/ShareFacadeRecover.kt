package movie.metropolis.app.screen.share

class ShareFacadeRecover(
    private val origin: ShareFacade
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation): Result<Unit> {
        return origin.runCatching { putTicket(ticket).getOrThrow() }
    }

}