package movie.core.preference

import movie.core.preference.EventPreference.OnChangedListener
import movie.settings.ObservablePreference.OnKeyChanged
import movie.settings.PreferenceStore

class EventPreferenceStored(
    private val store: PreferenceStore
) : EventPreference {

    override var filterSeen
        get() = store["filter-seen"]?.toBoolean() == true
        set(value) {
            store["filter-seen"] = value.toString()
        }

    override fun addListener(listener: OnChangedListener): OnChangedListener {
        val wrapped = listener.wrap()
        store.addListener(wrapped)
        return wrapped
    }

    override fun removeListener(listener: OnChangedListener) {
        store.removeListener(listener as? OnKeyChanged ?: return)
    }

    // ---

    private fun OnChangedListener.wrap() = object :
        OnChangedListener by this,
        OnKeyChanged {

        override fun onChanged(key: String) = onChanged()

    }

}