package movie.style.haptic

import android.os.Build
import android.view.HapticFeedbackConstants
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.material.ripple.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.platform.*
import kotlinx.coroutines.flow.filterIsInstance

fun HapticFeedback.click() {
    performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
}

fun HapticFeedback.tick() {
    performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
}

fun HapticFeedback.fastTick() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
        performHapticFeedback(HapticFeedbackConstants.SEGMENT_FREQUENT_TICK)
    } else {
        performHapticFeedback(HapticFeedbackConstants.CLOCK_TICK)
    }
}

fun HapticFeedback.performHapticFeedback(type: Int) {
    performHapticFeedback(HapticFeedbackType(type))
}

fun HapticFeedback.confirm() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.CONFIRM)
    }
}

fun HapticFeedback.reject() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        performHapticFeedback(HapticFeedbackConstants.REJECT)
    }
}

@Composable
fun <T> TickOnChange(value: T, key: Any? = Unit) {
    var last by rememberSaveable(key) { mutableStateOf(value) }
    val haptics = LocalHapticFeedback.current
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
    val haptics = LocalHapticFeedback.current
    LaunchedEffect(value) {
        if (last != value) {
            haptics.click()
            last = value
        }
    }
}

@Composable
fun rememberVibrateIndication(indication: Indication = rememberRipple()): Indication {
    return remember { VibrateIndication(indication) }
}

class VibrateIndication(
    private val origin: Indication
) : Indication {

    @Composable
    override fun rememberUpdatedInstance(interactionSource: InteractionSource): IndicationInstance {
        val haptic = LocalHapticFeedback.current
        LaunchedEffect(interactionSource) {
            interactionSource.interactions.filterIsInstance<PressInteraction.Release>().collect {
                haptic.click()
            }
        }
        return origin.rememberUpdatedInstance(interactionSource)
    }

}