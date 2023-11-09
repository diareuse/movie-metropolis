package movie.style.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.compose.ui.platform.*

@Composable
fun rememberSharedPrefs(name: String): SharedPreferences {
    val context = LocalContext.current
    return remember(context, name) { context.getSharedPreferences(name, Context.MODE_PRIVATE) }
}

fun SharedPreferences.getLongArray(key: String, default: LongArray): LongArray {
    return getString(key, null)?.split(',')?.map { it.toLong() }?.toLongArray() ?: default
}

fun SharedPreferences.Editor.putLongArray(key: String, value: LongArray) {
    putString(key, value.joinToString(",") { it.toString() })
}