package movie.metropolis.app.util

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.core.content.edit
import movie.style.util.rememberSharedPrefs

private const val UiStore = "ui-store"

@Composable
fun rememberStoreable(
    key: String,
    default: Float
): MutableState<Float> {
    val prefs = rememberSharedPrefs(UiStore)
    val state = rememberSaveable {
        mutableFloatStateOf(prefs.getFloat(key, default))
    }
    LaunchedEffect(state.value) {
        prefs.edit { putFloat(key, state.value) }
    }
    return state
}

@Composable
fun rememberStoreable(
    key: String,
    default: String?
): MutableState<String> {
    val prefs = rememberSharedPrefs(UiStore)
    val state = rememberSaveable {
        mutableStateOf(prefs.getString(key, default).orEmpty())
    }
    LaunchedEffect(state.value) {
        prefs.edit { putString(key, state.value) }
    }
    return state
}

@Composable
fun getStoreable(key: String, default: String, value: () -> String): String {
    val prefs = rememberSharedPrefs(UiStore)
    val newValue by remember(value) { derivedStateOf(value) }
    LaunchedEffect(newValue) {
        prefs.edit { putString(key, newValue) }
    }
    return remember(prefs, key) { prefs.getString(key, default) ?: default }
}