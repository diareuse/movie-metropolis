package movie.metropolis.app.screen.settings

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import movie.metropolis.app.screen.OnChangedListener

interface SettingsFacade {

    var filterSeen: Boolean

    fun addListener(listener: OnChangedListener): OnChangedListener
    fun removeListener(listener: OnChangedListener)

    companion object {

        val SettingsFacade.filterSeenFlow
            get() = callbackFlow {
                send(filterSeen)
                val listener = addListener {
                    trySend(filterSeen)
                }
                awaitClose {
                    removeListener(listener)
                }
            }.distinctUntilChanged()

    }

}