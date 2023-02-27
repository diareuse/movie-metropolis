package movie.style

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.window.*
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

val AnticipateOvershootEasing = object : Easing {
    val easing = AnticipateOvershootInterpolator()
    override fun transform(fraction: Float): Float {
        return easing.getInterpolation(fraction)
    }
}