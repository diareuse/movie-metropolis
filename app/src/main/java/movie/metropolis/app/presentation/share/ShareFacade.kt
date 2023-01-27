package movie.metropolis.app.presentation.share

interface ShareFacade {

    suspend fun putTicket(ticket: TicketRepresentation): Result<Unit>

}