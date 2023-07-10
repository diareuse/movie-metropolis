package movie.metropolis.app.screen.booking.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TicketActions(
    actions: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var anchorSize by remember { mutableFloatStateOf(1f) }
    val anchors = DraggableAnchors {
        0 at 0f
        1 at anchorSize
    }
    val state = remember {
        AnchoredDraggableState(
            initialValue = 0,
            anchors = anchors,
            positionalThreshold = { it },
            velocityThreshold = { 0f },
            animationSpec = tween()
        )
    }
    SideEffect {
        state.updateAnchors(anchors)
    }


    Box(
        modifier = modifier.anchoredDraggable(state, Orientation.Vertical),
        contentAlignment = Alignment.TopCenter
    ) {
        Row(
            modifier = Modifier.onGloballyPositioned { anchorSize = it.size.width.toFloat() },
            verticalAlignment = Alignment.CenterVertically,
            content = actions
        )
        Box(
            modifier = Modifier.offset {
                val offset = state.requireOffset().takeUnless { it.isNaN() } ?: 0f
                IntOffset(0, offset.roundToInt())
            }
        ) {
            content()
        }
    }

    // revert state back to initial
    LaunchedEffect(state.currentValue) {
        delay(5000)
        if (state.currentValue != 0)
            state.animateTo(0)
    }
}
