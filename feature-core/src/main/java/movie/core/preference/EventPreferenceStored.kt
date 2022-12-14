package movie.core.preference

import movie.settings.ObservablePreference
import movie.settings.PreferenceStore

class EventPreferenceStored(
    private val store: PreferenceStore
) : EventPreference, ObservablePreference by store {

    override var filterSeen
        get() = store["filter-seen"]?.toBoolean() == true
        set(value) {
            store["filter-seen"] = value.toString()
        }

}