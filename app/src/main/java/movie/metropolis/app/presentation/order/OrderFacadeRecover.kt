package movie.metropolis.app.presentation.order

import movie.log.flatMapCatching
import movie.log.logSevere

class OrderFacadeRecover(
    private val origin: OrderFacade
) : OrderFacade by origin {

    override suspend fun getRequest() =
        origin.flatMapCatching { getRequest() }.logSevere()

    override val isCompleted: Boolean
        get() = origin.runCatching { isCompleted }.logSevere().getOrDefault(false)

    override fun setUrl(url: String) {
        origin.runCatching { setUrl(url) }.logSevere()
    }

}