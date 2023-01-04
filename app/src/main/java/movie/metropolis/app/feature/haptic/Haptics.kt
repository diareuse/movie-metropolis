package movie.metropolis.app.feature.haptic

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

val Haptics
    @Composable get() = LocalHapticFeedback.current

fun <T> HapticFeedback.click(listener: () -> T): () -> Unit = {
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CONTEXT_CLICK))
    listener()
}

fun HapticFeedback.tick() =
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CLOCK_TICK))

@Composable
fun <T> hapticClick(listener: () -> T) = Haptics.click(listener)

@Composable
fun (() -> Any).withHaptics() = hapticClick(this)

@Composable
fun <T> TickOnChange(key: T) {
    var last by rememberSaveable { mutableStateOf(key) }
    val haptics = Haptics
    LaunchedEffect(key) {
        if (last != key) {
            haptics.tick()
            last = key
        }
    }
}