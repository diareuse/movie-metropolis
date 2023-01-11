package movie.core.preference

import movie.settings.PreferenceStore

class RegionPreferenceStored(
    private val store: PreferenceStore
) : RegionPreference {

    override var domain: String
        get() = store["domain"]?.takeUnless { it.isBlank() }.let(::requireNotNull)
        set(value) {
            store["domain"] = value
        }

    override var id: Int
        get() = store["id"]?.toIntOrNull().let(::requireNotNull)
        set(value) {
            store["id"] = value.toString()
        }

}