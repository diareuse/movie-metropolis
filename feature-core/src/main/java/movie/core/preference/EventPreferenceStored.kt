package movie.core.preference

import movie.core.util.Listenable
import movie.settings.PreferenceStore

class EventPreferenceStored(
    private val store: PreferenceStore
) : EventPreference {

    private val listenable = Listenable<EventPreference.OnChangedListener>()

    override var filterSeen
        get() = store["filter-seen"]?.toBoolean() == true
        set(value) {
            store["filter-seen"] = value.toString()
            listenable.notify { onChanged() }
        }
    override var onlyMovies: Boolean
        get() = store["only-movies"]?.toBoolean() == true
        set(value) {
            store["only-movies"] = value.toString()
            listenable.notify { onChanged() }
        }
    override var calendarId: String?
        get() = store["calendar-id"]
        set(value) {
            store["calendar-id"] = value
            listenable.notify { onChanged() }
        }
    override var distanceKms: Int
        get() = store["distance-kms"]?.toIntOrNull() ?: 20037
        set(value) {
            store["distance-kms"] = value.toString()
            listenable.notify { onChanged() }
        }
    override var keywords: List<String>
        get() = store["disabled-keywords"]?.split("\n").orEmpty()
        set(value) {
            store["disabled-keywords"] = value.joinToString("\n").takeUnless { it.isBlank() }
            listenable.notify { onChanged() }
        }

    override fun addOnChangedListener(listener: EventPreference.OnChangedListener): EventPreference.OnChangedListener {
        listenable += listener
        return listener
    }

    override fun removeOnChangedListener(listener: EventPreference.OnChangedListener): EventPreference.OnChangedListener {
        listenable -= listener
        return listener
    }

}