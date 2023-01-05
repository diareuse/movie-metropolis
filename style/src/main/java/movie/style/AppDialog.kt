package movie.style

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.random.Random.Default.nextLong

@Composable
internal fun AppDialog(
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

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppDialog(
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    AppDialog(
        isVisible = isVisible,
        onVisibilityChanged = onVisibilityChanged,
        exitDuration = 600,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(
                easing = AnticipateOvershootEasing,
                durationMillis = 600
            )
        ),
        exit = slideOutVertically(
            targetOffsetY = { it },
            animationSpec = tween(
                easing = AnticipateOvershootEasing,
                durationMillis = 600
            )
        ),
        content = content
    )
}

private val AnticipateOvershootEasing = object : Easing {
    val easing = AnticipateOvershootInterpolator()
    override fun transform(fraction: Float): Float {
        return easing.getInterpolation(fraction)
    }
}