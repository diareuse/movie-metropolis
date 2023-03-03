package movie.settings

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
class LazySharedPreferences(
    factory: () -> SharedPreferences
) : SharedPreferences {

    private val origin by lazy(LazyThreadSafetyMode.SYNCHRONIZED, factory)

    init {
        GlobalScope.launch(Dispatchers.IO) { origin.contains("") }
    }

    override fun getAll() =
        origin.all

    override fun getString(key: String?, defValue: String?) =
        origin.getString(key, defValue)

    override fun getStringSet(key: String?, defValues: MutableSet<String>?) =
        origin.getStringSet(key, defValues)

    override fun getInt(key: String?, defValue: Int) =
        origin.getInt(key, defValue)

    override fun getLong(key: String?, defValue: Long) =
        origin.getLong(key, defValue)

    override fun getFloat(key: String?, defValue: Float) =
        origin.getFloat(key, defValue)

    override fun getBoolean(key: String?, defValue: Boolean) =
        origin.getBoolean(key, defValue)

    override fun contains(key: String?) =
        origin.contains(key)

    override fun edit(): SharedPreferences.Editor =
        origin.edit()

    override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?) =
        origin.registerOnSharedPreferenceChangeListener(listener)

    override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener?) =
        origin.unregisterOnSharedPreferenceChangeListener(listener)

}