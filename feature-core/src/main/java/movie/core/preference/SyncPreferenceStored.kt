package movie.core.preference

import movie.settings.PreferenceStore
import java.util.Date

class SyncPreferenceStored(
    private val store: PreferenceStore
) : SyncPreference {

    override var previewUpcoming: Date
        get() = store["sync-preview-upcoming"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["sync-preview-upcoming"] = value.time.toString()
        }
    override var previewCurrent: Date
        get() = store["sync-preview-current"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["sync-preview-current"] = value.time.toString()
        }
    override var cinema: Date
        get() = store["sync-cinema"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["sync-cinema"] = value.time.toString()
        }
    override var booking: Date
        get() = store["sync-booking"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["sync-booking"] = value.time.toString()
        }
}