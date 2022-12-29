package movie.core.preference

import movie.settings.PreferenceStore

class EventPreferenceStored(
    private val store: PreferenceStore
) : EventPreference {

    override var filterSeen
        get() = store["filter-seen"]?.toBoolean() == true
        set(value) {
            store["filter-seen"] = value.toString()
        }
    override var calendarId: String?
        get() = store["calendar-id"]
        set(value) {
            store["calendar-id"] = value
        }

}