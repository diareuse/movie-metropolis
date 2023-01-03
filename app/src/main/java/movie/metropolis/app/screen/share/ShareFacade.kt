package movie.metropolis.app.screen.share

interface ShareFacade {

    suspend fun putTicket(ticket: TicketRepresentation): Result<Unit>

}