package movie.metropolis.app.feature.haptic

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback


val Haptics
    @Composable get() = LocalHapticFeedback.current

fun HapticFeedback.click(listener: () -> Any): () -> Unit = {
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CONTEXT_CLICK))
    listener()
}

fun HapticFeedback.tick() =
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CLOCK_TICK))

@Composable
fun hapticClick(listener: () -> Any) = Haptics.click(listener)

@Composable
fun (() -> Any).withHaptics() = hapticClick(this)