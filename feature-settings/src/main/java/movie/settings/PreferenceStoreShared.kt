package movie.settings

import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceStoreShared(
    private val prefs: SharedPreferences
) : PreferenceStore {

    override fun set(key: String, value: String?) {
        prefs.edit {
            putString(key, value)
        }
    }

    override fun get(key: String): String? {
        return prefs.getString(key, null)
    }

}