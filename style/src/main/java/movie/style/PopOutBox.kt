package movie.style

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.hapticfeedback.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.core.view.WindowCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import movie.style.haptic.tick
import movie.style.layout.PreviewLayout
import movie.style.util.toDpOffset
import movie.style.util.toDpSize

fun Modifier.popOutBackground(state: OverlayState) = composed {
    val blur by animateDpAsState(targetValue = if (state.expanded) 32.dp else 0.dp)
    val alpha by animateFloatAsState(targetValue = if (state.expanded) .5f else 1f)
    alpha(alpha).blur(blur)
}

@Stable
class OverlayState {
    var expanded: Boolean by mutableStateOf(false)
}

@Composable
fun OverlayState.rememberPopOutState(): PopOutState {
    val haptics = LocalHapticFeedback.current
    return remember(this, haptics) {
        PopOutState(this, haptics)
    }
}

@Stable
class PopOutState(
    private val scope: OverlayState,
    private val haptics: HapticFeedback
) {
    var expanded by mutableStateOf(false)
        private set
    var expansionAlpha by mutableStateOf(0f)
        private set
    var scale by mutableStateOf(0f)
        private set
    var shadowAlpha by mutableStateOf(1f)
        private set

    var offset by mutableStateOf(DpOffset.Zero)
        private set
    var size by mutableStateOf(DpSize.Zero)
        private set
    var horizontalAlignment by mutableStateOf(Alignment.CenterHorizontally)
        private set
    var verticalAlignment by mutableStateOf(Alignment.Top)
        private set

    internal fun updatePosition(
        it: LayoutCoordinates,
        direction: LayoutDirection,
        density: Density
    ) {
        val rootSize = it.findRootCoordinates().size.toDpSize(density)
        offset = it.positionInWindow().toDpOffset(density)
        size = it.size.toDpSize(density)
        verticalAlignment = when (offset.y) {
            in 0.dp..(rootSize.height / 2) -> Alignment.Top
            else -> Alignment.Bottom
        }
        horizontalAlignment = when (offset.x + 2.dp) {
            in 0.dp..((rootSize.width / 2) - (size.width / 2)) -> when (direction) {
                LayoutDirection.Ltr -> Alignment.Start
                LayoutDirection.Rtl -> Alignment.End
            }

            in ((rootSize.width / 2) + (size.width / 2))..rootSize.width -> when (direction) {
                LayoutDirection.Ltr -> Alignment.End
                LayoutDirection.Rtl -> Alignment.Start
            }

            else -> Alignment.CenterHorizontally
        }
    }

    suspend fun open() {
        expanded = true
        shadowAlpha = 0f
        scope.expanded = true
        animate(scale, 1.1f) { it, _ -> scale = it }
        haptics.tick()
        animate(1.1f, 1f) { it, _ -> scale = it }
        haptics.tick()
        animate(expansionAlpha, 1f) { it, _ -> expansionAlpha = it }
    }

    suspend fun close() {
        animate(expansionAlpha, 0f) { it, _ -> expansionAlpha = it }
        scope.expanded = false
        animate(scale, 1.1f) { it, _ -> scale = it }
        haptics.tick()
        animate(1.1f, 1f) { it, _ -> scale = it }
        haptics.tick()
        shadowAlpha = 1f
        delay(100)
        expanded = false
    }

    companion object {
        val Saver = mapSaver({ buildMap { } }, { })
    }

}

@Composable
fun PopOutBox(
    state: PopOutState,
    modifier: Modifier = Modifier,
    expansion: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier,
        propagateMinConstraints = true
    ) {
        val direction = LocalLayoutDirection.current
        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .onGloballyPositioned { state.updatePosition(it, direction, density) }
                .alpha(state.shadowAlpha),
            propagateMinConstraints = true
        ) {
            content()
        }
    }
    val statusBars = WindowInsets.statusBars.asPaddingValues()
    val scope = rememberCoroutineScope()
    if (state.expanded) Dialog(
        onDismissRequest = {
            scope.launch { state.close() }
        },
        properties = DialogProperties(
            dismissOnBackPress = false,
            decorFitsSystemWindows = false,
            usePlatformDefaultWidth = false
        )
    ) {
        val view = LocalView.current
        SideEffect {
            (view.parent as? DialogWindowProvider)?.window?.let { window ->
                WindowCompat.setDecorFitsSystemWindows(window, false)
                window.setWindowAnimations(-1)
                window.setDimAmount(0f)
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { scope.launch { state.close() } }
                }
        ) {
            BackHandler {
                scope.launch {
                    state.close()
                }
            }
            val expansion = @Composable {
                Box(
                    modifier = Modifier
                        .alpha(state.expansionAlpha)
                        .scale(state.scale)
                ) {
                    expansion()
                }
            }
            val top = if (state.verticalAlignment === Alignment.Bottom) expansion else null
            val bottom = if (state.verticalAlignment === Alignment.Top) expansion else null
            AnchoredLayout(
                modifier = Modifier
                    .offset(state.offset.x, state.offset.y - statusBars.calculateTopPadding()),
                horizontalAlignment = state.horizontalAlignment,
                top = top,
                bottom = bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(state.size)
                        .scale(state.scale),
                    propagateMinConstraints = true
                ) {
                    content()
                }
            }
        }
    }
}

@Composable
fun AnchoredLayout(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    top: (@Composable () -> Unit)? = null,
    bottom: (@Composable () -> Unit)? = null,
    anchor: @Composable () -> Unit,
) = Layout(
    modifier = modifier,
    content = {
        Box(modifier = Modifier.layoutId("anchor")) { anchor() }
        if (top != null) Box(modifier = Modifier.layoutId("top")) { top() }
        if (bottom != null) Box(modifier = Modifier.layoutId("bottom")) { bottom() }
    }
) { measurables, constraints ->
    val anchor = measurables.first { it.layoutId == "anchor" }.measure(constraints)
    val top = measurables.find { it.layoutId == "top" }?.measure(constraints)
    val bottom = measurables.find { it.layoutId == "bottom" }?.measure(constraints)
    val width = maxOf(anchor.width, top?.width ?: 0, bottom?.width ?: 0)
    val height = anchor.height + (top?.height ?: 0) + (bottom?.height ?: 0)
    layout(width, height) {
        anchor.placeRelative(IntOffset.Zero)
        top?.placeRelative(
            x = horizontalAlignment.align(top.width, anchor.width, layoutDirection),
            y = -top.height
        )
        bottom?.placeRelative(
            x = horizontalAlignment.align(bottom.width, anchor.width, layoutDirection),
            y = anchor.height
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogClonePreview() {
    val overlay = remember { OverlayState() }
    PreviewLayout(modifier = Modifier.popOutBackground(overlay)) {
        val state = overlay.rememberPopOutState()
        PopOutBox(
            state = state,
            modifier = Modifier
                .padding(100.dp, 200.dp)
                .wrapContentSize(),
            expansion = {
                Button(onClick = { /*TODO*/ }) {
                    Text("Foooo")
                }
            }
        ) {
            val scope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .clickable { scope.launch { state.open() } }
                    .size(100.dp, 150.dp)
                    .background(Color.Green)
            )
        }
    }
}