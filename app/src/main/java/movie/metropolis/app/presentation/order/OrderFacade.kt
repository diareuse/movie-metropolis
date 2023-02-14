package movie.metropolis.app.presentation.order

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.presentation.asLoadable
import movie.metropolis.app.util.throttleWithTimeout
import kotlin.time.Duration.Companion.seconds

interface OrderFacade {

    val isCompleted: Boolean

    suspend fun getRequest(): Result<RequestView>
    fun setUrl(url: String)

    fun addOnChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnChangedListener(listener: OnChangedListener)

    fun interface Factory {
        fun create(url: String): OrderFacade
    }

    companion object {

        val OrderFacade.requestFlow
            get() = channelFlow {
                send(getRequest().asLoadable())
            }.throttleWithTimeout(1.seconds)

        val OrderFacade.isCompletedFlow
            get() = callbackFlow {
                val listener = addOnChangedListener {
                    trySend(isCompleted)
                }
                send(false)
                awaitClose {
                    removeOnChangedListener(listener)
                }
            }

    }

}