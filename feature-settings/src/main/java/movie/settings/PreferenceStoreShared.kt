package movie.settings

import android.content.SharedPreferences
import androidx.core.content.edit

class PreferenceStoreShared(
    private val prefs: SharedPreferences,
    private val observer: ObservablePreference
) : PreferenceStore, ObservablePreference by observer {

    override fun set(key: String, value: String?) {
        prefs.edit {
            putString(key, value)
        }
        notify(key)
    }

    override fun get(key: String): String? {
        return prefs.getString(key, null)
    }

}