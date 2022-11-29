package movie.metropolis.app.screen.booking

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random.Default.nextLong

@Composable
fun AnimatedDialog(
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    exitDuration: Int = 600,
    properties: DialogProperties = DialogProperties(),
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isDialogVisible by remember { mutableStateOf(false) }
    val mutex = remember { Mutex(true) }
    val token = remember(isVisible) { nextLong() }

    LaunchedEffect(token) {
        if (isVisible) launch {
            mutex.withLock {
                isDialogVisible = true
            }
        }
    }

    if (isVisible) Dialog(
        onDismissRequest = {
            coroutineScope.launch {
                mutex.lock()
                isDialogVisible = false
                delay(exitDuration.toLong())
                onVisibilityChanged(false)
            }
        },
        properties = properties
    ) {
        SideEffect {
            if (mutex.isLocked) mutex.unlock()
        }
        AnimatedVisibility(
            visible = isDialogVisible,
            enter = enter,
            exit = exit
        ) {
            content()
        }
    }
}