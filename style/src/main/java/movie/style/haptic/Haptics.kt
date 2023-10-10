package movie.style.haptic

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
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CONTEXT_CLICK))
}

fun HapticFeedback.tick() {
    performHapticFeedback(HapticFeedbackType(HapticFeedbackConstants.CLOCK_TICK))
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