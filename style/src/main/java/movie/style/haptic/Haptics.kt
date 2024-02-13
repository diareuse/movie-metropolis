package movie.style.haptic

import android.os.Build
import android.view.HapticFeedbackConstants
import android.view.ViewConfiguration
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.node.*
import androidx.compose.ui.platform.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
fun rememberVibrateIndication(): Indication {
    val haptic = LocalHapticFeedback.current
    return remember { VibrateIndicationNodeFactory(haptic) }
}

class VibrateIndicationNodeFactory(
    private val haptic: HapticFeedback
) : IndicationNodeFactory {
    override fun create(interactionSource: InteractionSource): DelegatableNode {
        return VibrateIndication(haptic, interactionSource)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is VibrateIndicationNodeFactory) return false

        if (haptic != other.haptic) return false

        return true
    }

    override fun hashCode(): Int {
        return haptic.hashCode()
    }

}

class VibrateIndication(
    private val haptic: HapticFeedback,
    private val interactionSource: InteractionSource
) : Modifier.Node(), DrawModifierNode {

    private var currentPressPosition: Offset = Offset.Zero
    private val animatedScalePercent = Animatable(1f)

    private suspend fun animateToPressed(pressPosition: Offset) {
        currentPressPosition = pressPosition
        animatedScalePercent.animateTo(0.9f, spring())
    }

    private suspend fun animateToResting() {
        animatedScalePercent.animateTo(1f, spring())
    }


    override fun onAttach() {
        coroutineScope.launch {
            var lastPress = 0L
            val timeout = ViewConfiguration.getLongPressTimeout().toLong()
            interactionSource.interactions.collectLatest { interaction ->
                when (interaction) {
                    is PressInteraction.Press -> {
                        lastPress = System.currentTimeMillis()
                        haptic.fastTick()
                        animateToPressed(interaction.pressPosition)
                    }

                    is PressInteraction.Release -> {
                        if (timeout > System.currentTimeMillis() - lastPress)
                            haptic.click()
                        animateToResting()
                    }

                    is PressInteraction.Cancel -> animateToResting()
                }
            }
        }
    }

    override fun ContentDrawScope.draw() {
        scale(
            scale = animatedScalePercent.value,
            pivot = currentPressPosition
        ) {
            this@draw.drawContent()
        }
    }

}