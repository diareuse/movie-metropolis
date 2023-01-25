package movie.metropolis.app.screen.settings

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import movie.metropolis.app.screen.OnChangedListener

interface SettingsFacade {

    var filterSeen: Boolean
    var onlyMovies: Boolean
    val addToCalendar: Boolean
    var clipRadius: Int

    suspend fun getCalendars(): Map<String, String>
    fun selectCalendar(id: String?)

    fun addListener(listener: OnChangedListener): OnChangedListener
    fun removeListener(listener: OnChangedListener)

    companion object {

        private val SettingsFacade.listenerFlow
            get() = callbackFlow {
                send(Any())
                val listener = addListener {
                    trySend(Any())
                }
                awaitClose {
                    removeListener(listener)
                }
            }

        val SettingsFacade.filterSeenFlow
            get() = listenerFlow.map { filterSeen }.distinctUntilChanged()

        val SettingsFacade.onlyMoviesFlow
            get() = listenerFlow.map { onlyMovies }.distinctUntilChanged()

        val SettingsFacade.addToCalendarFlow
            get() = listenerFlow.map { addToCalendar }.distinctUntilChanged()

        val SettingsFacade.calendarFlow
            get() = listenerFlow.map { getCalendars() }.distinctUntilChanged()

        val SettingsFacade.clipRadiusFlow
            get() = listenerFlow.map { clipRadius }.distinctUntilChanged()

    }

}