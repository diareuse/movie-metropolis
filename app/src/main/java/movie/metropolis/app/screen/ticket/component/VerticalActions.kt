@file:OptIn(ExperimentalFoundationApi::class)

package movie.metropolis.app.screen.ticket.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch
import movie.style.layout.PreviewLayout

@Composable
fun <T : Any> VerticalActions(
    state: AnchoredDraggableState<T>,
    actions: @Composable DraggableAnchorScope<T>.() -> Unit,
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(16.dp),
    enabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    val initial = remember(Unit) { state.currentValue }
    val positions = remember { mutableStateMapOf<T, Float>() }
    val anchors = DraggableAnchors {
        for ((key, value) in positions)
            key at value
    }
    SideEffect {
        state.updateAnchors(anchors)
    }
    Box(
        modifier = modifier,
        propagateMinConstraints = true
    ) {
        val scope = rememberCoroutineScope()
        Column(
            modifier = Modifier
                .anchoredDraggable(state, Orientation.Vertical, enabled)
                .wrapContentHeight(Alignment.Top),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement
        ) {
            remember(this) {
                DraggableAnchorScopeImpl<T>(this) { key, value ->
                    positions[key] = value
                }
            }.apply {
                Box(Modifier.anchor(initial))
                actions()
            }
        }
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(x = 0, y = state.offset.toInt())
                }
                .pointerInput(Unit) {
                    var velocity = 0f
                    detectVerticalDragGestures(
                        onDragEnd = {
                            scope.launch {
                                state.settle(velocity)
                            }
                        }
                    ) { change, dragAmount ->
                        velocity = change
                            .positionChange()
                            .getDistance()
                        state.dispatchRawDelta(dragAmount)
                        change.consume()
                    }
                },
            propagateMinConstraints = true
        ) {
            content()
        }
    }
}

interface DraggableAnchorScope<T : Any> : ColumnScope {
    fun Modifier.anchor(key: T): Modifier
}

class DraggableAnchorScopeImpl<T : Any>(
    scope: ColumnScope,
    private val onAnchorChange: (T, Float) -> Unit,
) : DraggableAnchorScope<T>, ColumnScope by scope {

    override fun Modifier.anchor(key: T): Modifier {
        return onGloballyPositioned {
            onAnchorChange(key, it.positionInParent().y + it.size.height)
        }
    }

}

@Composable
fun <T : Any> rememberAnchoredDraggableState(
    initialValue: T,
    animationSpec: AnimationSpec<Float> = tween()
): AnchoredDraggableState<T> {
    return remember {
        AnchoredDraggableState(
            initialValue = initialValue,
            anchors = DraggableAnchors {},
            positionalThreshold = { distance -> distance * 0.5f },
            velocityThreshold = { 0.001f },
            animationSpec = animationSpec
        )
    }
}

@Preview
@Composable
private fun VerticalActionsPreview() = PreviewLayout {
    VerticalActions(
        modifier = Modifier.fillMaxSize(),
        state = rememberAnchoredDraggableState(0),
        actions = {
            Box(Modifier.anchor(0))
            Box(
                Modifier
                    .anchor(1)
                    .width(48.dp)
                    .height(48.dp)
                    .background(Color.Green)
            )
            Box(
                Modifier
                    .anchor(2)
                    .width(48.dp)
                    .height(48.dp)
                    .background(Color.Magenta)
            )
        }
    ) {
        Box(Modifier.background(Color.Blue))
    }
}