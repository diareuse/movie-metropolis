package movie.settings

interface PreferenceStore {

    operator fun set(key: String, value: String?)
    operator fun get(key: String): String?

}