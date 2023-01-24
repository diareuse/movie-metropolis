package movie.core.preference

import movie.settings.PreferenceStore
import java.util.Date

class SyncPreferenceStored(
    private val store: PreferenceStore
) : SyncPreference {

    override var previewUpcoming: Date
        get() = store["preview-upcoming"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["preview-upcoming"] = value.time.toString()
        }
    override var previewCurrent: Date
        get() = store["preview-current"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["preview-current"] = value.time.toString()
        }
    override var cinema: Date
        get() = store["cinema"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["cinema"] = value.time.toString()
        }
    override var booking: Date
        get() = store["booking"]?.toLongOrNull()?.let(::Date) ?: Date(0)
        set(value) {
            store["booking"] = value.time.toString()
        }
}