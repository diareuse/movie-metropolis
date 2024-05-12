package movie.style.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*
import okio.withLock
import java.util.concurrent.locks.ReentrantLock

@Composable
fun rememberSharedPrefs(name: String): SharedPreferences {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    return remember(name) {
        UiSharedPreferences {
            context.getSharedPreferences(name, Context.MODE_PRIVATE)
        }
    }
}

fun SharedPreferences.getLongArray(key: String, default: LongArray): LongArray {
    return getString(key, null)?.split(',')?.map { it.toLong() }?.toLongArray() ?: default
}

fun SharedPreferences.Editor.putLongArray(key: String, value: LongArray) {
    putString(key, value.joinToString(",") { it.toString() })
}

private class UiSharedPreferences(
    private val factory: () -> SharedPreferences
) : SharedPreferences {

    private val lock = ReentrantLock()
    private var origin: SharedPreferences? = null
        get() = field ?: lock.withLock {
            field ?: factory().also {
                field = it
            }
        }

    override fun getAll(): Map<String, *> =
        origin!!.all

    override fun getString(key: String?, defValue: String?) =
        origin!!.getString(key, defValue)

    override fun getStringSet(key: String?, defValues: Set<String>?) =
        origin!!.getStringSet(key, defValues)

    override fun getInt(key: String?, defValue: Int) =
        origin!!.getInt(key, defValue)

    override fun getLong(key: String?, defValue: Long) =
        origin!!.getLong(key, defValue)

    override fun getFloat(key: String?, defValue: Float) =
        origin!!.getFloat(key, defValue)

    override fun getBoolean(key: String?, defValue: Boolean) =
        origin!!.getBoolean(key, defValue)

    override fun contains(key: String?) =
        origin!!.contains(key)

    override fun edit() =
        origin!!.edit()

    override fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) =
        origin!!.registerOnSharedPreferenceChangeListener(listener)

    override fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener?) =
        origin!!.unregisterOnSharedPreferenceChangeListener(listener)

}