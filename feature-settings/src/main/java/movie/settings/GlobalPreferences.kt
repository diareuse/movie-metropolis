package movie.settings

import android.content.SharedPreferences
import androidx.core.content.edit

class GlobalPreferences internal constructor(
    private val prefs: SharedPreferences
) {

    var filterSeen
        get() = prefs.getBoolean("filter-seen", false)
        set(value) = prefs.edit {
            putBoolean("filter-seen", value)
        }

    var onlyMovies
        get() = prefs.getBoolean("movies-only", false)
        set(value) = prefs.edit {
            putBoolean("movies-only", value)
        }

    var calendarId
        get() = prefs.getString("calendar", null)
        set(value) = prefs.edit {
            putString("calendar", value)
        }

    var distanceKms
        get() = prefs.getInt("distance", 20037)
        set(value) = prefs.edit {
            putInt("distance", value.coerceAtMost(20037))
        }

    var keywords
        get() = prefs.getStringSet("keywords", null).orEmpty()
        set(value) = prefs.edit {
            putStringSet("keywords", value)
        }

    var regionId
        get() = prefs.getString("id", null)?.toIntOrNull()
        set(value) = prefs.edit {
            putString("id", value?.toString())
        }

}