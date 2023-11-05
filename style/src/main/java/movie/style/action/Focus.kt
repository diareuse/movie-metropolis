package movie.style.action

import androidx.compose.runtime.*
import androidx.compose.ui.platform.*

@Composable
fun <T> clearFocus(): (T) -> Unit {
    val manager = LocalFocusManager.current
    return { manager.clearFocus() }
}