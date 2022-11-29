package movie.metropolis.app.screen.booking

import android.view.animation.AnticipateOvershootInterpolator
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.DialogProperties

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AppDialog(
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedDialog(
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