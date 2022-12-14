package movie.metropolis.app.screen.settings

import movie.core.preference.EventPreference
import movie.metropolis.app.screen.settings.SettingsFacade.OnChangedListener
import java.util.Collections

class SettingsFacadeFromFeature(
    private val prefs: EventPreference
) : SettingsFacade {

    val listeners: MutableSet<OnChangedListener> =
        Collections.synchronizedSet(mutableSetOf<OnChangedListener>())

    override var filterSeen: Boolean
        get() = prefs.filterSeen
        set(value) {
            prefs.filterSeen = value
        }

    override fun addListener(listener: OnChangedListener): OnChangedListener {
        listeners += listener
        return listener
    }

    override fun removeListener(listener: OnChangedListener) {
        listeners -= listener
    }

}

