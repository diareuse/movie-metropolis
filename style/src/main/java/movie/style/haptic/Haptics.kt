package movie.style.haptic

import android.view.HapticFeedbackConstants
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.platform.*

val Haptics
    @Composable get() = LocalHapticFeedback.current

fun <T> HapticFeedback.click(listener: () -> T): () -> Unit = {
    click()
    listener()
}

fun HapticFeedback.click() {
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CONTEXT_CLICK))
}

fun HapticFeedback.tick() {
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CLOCK_TICK))
}

@Composable
fun <T> hapticClick(listener: () -> T) = Haptics.click(listener)

@Composable
fun (() -> Any).withHaptics() = hapticClick(this)

@Composable
fun <T> TickOnChange(value: T, key: Any? = Unit) {
    var last by rememberSaveable(key) { mutableStateOf(value) }
    val haptics = Haptics
    LaunchedEffect(value) {
        if (last != value) {
            haptics.tick()
            last = value
        }
    }
}

@Composable
fun <T> ClickOnChange(value: T, key: Any? = Unit) {
    var last by rememberSaveable(key) { mutableStateOf(value) }
    val haptics = Haptics
    LaunchedEffect(value) {
        if (last != value) {
            haptics.click()
            last = value
        }
    }
}