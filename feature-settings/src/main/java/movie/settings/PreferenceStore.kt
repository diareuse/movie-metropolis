package movie.settings

interface PreferenceStore : ObservablePreference {

    operator fun set(key: String, value: String?)
    operator fun get(key: String): String?

}