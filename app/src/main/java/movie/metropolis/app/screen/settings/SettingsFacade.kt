package movie.metropolis.app.screen.settings

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

interface SettingsFacade {

    var filterSeen: Boolean

    fun addOnPreferenceChangedListener(listener: OnPreferenceChangedListener): OnPreferenceChangedListener
    fun removeOnPreferenceChangedListener(listener: OnPreferenceChangedListener)

    fun interface OnPreferenceChangedListener {
        fun onChanged()
    }

    companion object {

        val SettingsFacade.filterSeenFlow
            get() = callbackFlow {
                send(filterSeen)
                val listener = addOnPreferenceChangedListener {
                    trySend(filterSeen)
                }
                awaitClose {
                    removeOnPreferenceChangedListener(listener)
                }
            }.distinctUntilChanged()

    }

}