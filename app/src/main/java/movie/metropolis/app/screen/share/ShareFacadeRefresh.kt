package movie.metropolis.app.screen.share

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import movie.core.UserFeature

class ShareFacadeRefresh(
    private val origin: ShareFacade,
    private val user: UserFeature
) : ShareFacade {

    override suspend fun putTicket(ticket: TicketRepresentation): Result<Unit> {
        return origin.putTicket(ticket).onSuccess {
            coroutineScope {
                launch {
                    user.getBookings()
                }
            }
        }
    }

}