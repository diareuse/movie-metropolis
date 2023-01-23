package movie.metropolis.app.screen.order

import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import movie.metropolis.app.screen.asLoadable
import kotlin.time.Duration.Companion.seconds

interface OrderFacade {

    suspend fun getRequest(): Result<RequestView>

    fun interface Factory {
        fun create(url: String): OrderFacade
    }

    companion object {

        val OrderFacade.requestFlow
            get() = channelFlow {
                send(getRequest().asLoadable())
            }.debounce(1.seconds)

    }

}