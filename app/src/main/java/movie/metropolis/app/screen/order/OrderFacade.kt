package movie.metropolis.app.screen.order

import kotlinx.coroutines.flow.channelFlow
import movie.metropolis.app.screen.asLoadable

interface OrderFacade {

    suspend fun getRequest(): Result<RequestView>

    fun interface Factory {
        fun create(url: String): OrderFacade
    }

    companion object {

        val OrderFacade.requestFlow
            get() = channelFlow {
                send(getRequest().asLoadable())
            }

    }

}