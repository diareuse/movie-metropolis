package movie.metropolis.app.screen.settings

import movie.core.preference.EventPreference

class SettingsFacadeFromFeature(
    private val prefs: EventPreference
) : SettingsFacade {

    override var filterSeen: Boolean
        get() = prefs.filterSeen
        set(value) {
            prefs.filterSeen = value
        }

    override fun addListener(listener: SettingsFacade.OnChangedListener): SettingsFacade.OnChangedListener {
        val wrapped = listener.wrap()
        prefs.addListener(wrapped)
        return wrapped
    }

    override fun removeListener(listener: SettingsFacade.OnChangedListener) {
        prefs.removeListener(listener as? EventPreference.OnChangedListener ?: return)
    }

    private fun SettingsFacade.OnChangedListener.wrap() = object :
        SettingsFacade.OnChangedListener by this,
        EventPreference.OnChangedListener {}

}