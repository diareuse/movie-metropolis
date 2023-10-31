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
    override var onlyMovies: Boolean
        get() = store["only-movies"]?.toBoolean() == true
        set(value) {
            store["only-movies"] = value.toString()
        }
    override var calendarId: String?
        get() = store["calendar-id"]
        set(value) {
            store["calendar-id"] = value
        }
    override var distanceKms: Int
        get() = store["distance-kms"]?.toIntOrNull() ?: 20037
        set(value) {
            store["distance-kms"] = value.toString()
        }

}