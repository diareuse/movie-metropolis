package movie.metropolis.app.screen.order

class OrderFacadeRecover(
    private val origin: OrderFacade
) : OrderFacade {

    override suspend fun getRequest() =
        origin.runCatching { getRequest().getOrThrow() }

}