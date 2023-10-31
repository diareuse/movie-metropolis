package movie.metropolis.app.util

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.platform.*
import androidx.core.content.edit

@Composable
fun rememberStoreable(
    key: String,
    default: Float
): MutableState<Float> {
    val prefs = rememberSharedPrefs(name = "ui-store")
    val state = rememberSaveable {
        mutableFloatStateOf(prefs.getFloat(key, default))
    }
    LaunchedEffect(state.value) {
        prefs.edit { putFloat(key, state.value) }
    }
    return state
}

@Composable
fun rememberSharedPrefs(name: String): SharedPreferences {
    val context = LocalContext.current
    return remember(context, name) { context.getSharedPreferences(name, Context.MODE_PRIVATE) }
}