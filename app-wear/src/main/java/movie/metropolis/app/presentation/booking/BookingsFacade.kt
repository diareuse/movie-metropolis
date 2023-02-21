package movie.metropolis.app.presentation.booking

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.debounce
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.presentation.OnChangedListener
import movie.metropolis.app.presentation.asLoadable
import kotlin.time.Duration.Companion.seconds

interface BookingsFacade {

    suspend fun get(): Result<List<TicketView>>
    fun addOnChangedListener(listener: OnChangedListener): OnChangedListener
    fun removeOnChangedListener(listener: OnChangedListener)

    companion object {

        private val BookingsFacade.onChangedFlow
            get() = callbackFlow {
                val listener = addOnChangedListener {
                    trySend(Any())
                }
                awaitClose {
                    removeOnChangedListener(listener)
                }
            }

        val BookingsFacade.flow
            get() = channelFlow {
                send(get().asLoadable())
                onChangedFlow.debounce(1.seconds).collect {
                    send(get().asLoadable())
                }
            }

    }

}