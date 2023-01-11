package movie.metropolis.app.screen.order

import movie.log.logCatchingResult

class OrderFacadeRecover(
    private val origin: OrderFacade
) : OrderFacade {

    override suspend fun getRequest() =
        origin.logCatchingResult("order") { getRequest() }

}