package movie.style.layout

import androidx.compose.runtime.*
import androidx.compose.ui.focus.*

@Composable
fun rememberFocusRequester(requestFocus: Boolean = false): FocusRequester {
    return remember { FocusRequester() }.also {
        if (requestFocus) LaunchedEffect(it) {
            it.requestFocus()
        }
    }
}