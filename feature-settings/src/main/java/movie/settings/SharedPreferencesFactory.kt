package movie.settings

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesFactory private constructor(
    private val name: String
) {

    fun create(context: Context): SharedPreferences = LazySharedPreferences {
        val name = context.packageName + ".$name"
        context.getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    companion object {

        fun user() = SharedPreferencesFactory("user")
        fun functionality() = SharedPreferencesFactory("functionality")

    }

}