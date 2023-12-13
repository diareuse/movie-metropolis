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
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.*
import androidx.core.view.WindowCompat
import kotlinx.coroutines.launch
import movie.style.layout.PreviewLayout
import movie.style.util.toDpSize
import kotlin.math.roundToInt

@Composable
fun OverlayContainer(
    modifier: Modifier = Modifier,
    content: @Composable OverlayScope.() -> Unit,
) {
    val scope = remember { OverlayScopeImpl() }
    val blur by animateDpAsState(targetValue = if (scope.expanded) 32.dp else 0.dp)
    val alpha by animateFloatAsState(targetValue = if (scope.expanded) .5f else 1f)
    Box(
        modifier = modifier
            .alpha(alpha)
            .blur(blur),
        propagateMinConstraints = true
    ) {
        content(scope)
    }
}

@Stable
interface OverlayScope {
    var expanded: Boolean
}

@Stable
class OverlayScopeImpl : OverlayScope {
    override var expanded: Boolean by mutableStateOf(false)
}

val Offset.Companion.Saver
    get() = mapSaver(
        { mapOf("x" to it.x, "y" to it.y) },
        { Offset(it["x"] as Float, it["y"] as Float) }
    )

val IntSize.Companion.Saver
    get() = mapSaver(
        { mapOf("w" to it.width, "h" to it.height) },
        { IntSize(it["w"] as Int, it["h"] as Int) }
    )

val Alignment.Companion.HorizontalSaver
    get() = mapSaver(
        {
            mapOf(
                "t" to when (this) {
                    Start -> "Start"
                    CenterHorizontally -> "CenterHorizontally"
                    End -> "End"
                    else -> "Start"
                }
            )
        },
        {
            when (it["t"] as String) {
                "Start" -> Start
                "CenterHorizontally" -> CenterHorizontally
                "End" -> End
                else -> Start
            }
        }
    )

val Alignment.Companion.VerticalSaver
    get() = mapSaver(
        {
            mapOf(
                "t" to when (this) {
                    Top -> "Top"
                    CenterVertically -> "CenterVertically"
                    Bottom -> "Bottom"
                    else -> "Top"
                }
            )
        },
        {
            when (it["t"] as String) {
                "Top" -> Top
                "CenterVertically" -> CenterVertically
                "Bottom" -> Bottom
                else -> Top
            }
        }
    )

@Composable
fun OverlayScope.rememberDialogCloneState(key: Any? = null) = remember(this, key) {
    DialogCloneState(this)
}

@Stable
class DialogCloneState(
    private val scope: OverlayScope
) {
    var expanded by mutableStateOf(false)
        private set
    var alpha by mutableStateOf(0f)
        private set
    var scale by mutableStateOf(0f)
        private set

    suspend fun open() {
        expanded = true
        scope.expanded = true
        animate(scale, 1.1f) { it, _ -> scale = it }
        animate(1.1f, 1f) { it, _ -> scale = it }
        animate(alpha, 1f) { it, _ -> alpha = it }
    }

    suspend fun close() {
        animate(alpha, 0f) { it, _ -> alpha = it }
        scope.expanded = false
        animate(scale, 1.1f) { it, _ -> scale = it }
        animate(1.1f, 1f) { it, _ -> scale = it }
        expanded = false
    }
}

@Composable
fun DialogClone(
    state: DialogCloneState,
    modifier: Modifier = Modifier,
    expansion: @Composable () -> Unit,
    content: @Composable () -> Unit,
) {
    var offset by rememberSaveable(stateSaver = Offset.Saver) { mutableStateOf(Offset.Zero) }
    var size by rememberSaveable(stateSaver = IntSize.Saver) { mutableStateOf(IntSize.Zero) }
    var horizontalAlignment by rememberSaveable(stateSaver = Alignment.HorizontalSaver) {
        mutableStateOf(Alignment.CenterHorizontally)
    }
    var verticalAlignment by rememberSaveable(stateSaver = Alignment.VerticalSaver) {
        mutableStateOf(Alignment.Top)
    }
    Box(
        modifier = modifier,
        propagateMinConstraints = true
    ) {
        val direction = LocalLayoutDirection.current
        Box(
            modifier = Modifier.onGloballyPositioned {
                val rootSize = it.findRootCoordinates().size
                offset = it.positionInWindow()
                size = it.size
                verticalAlignment = when (offset.y.roundToInt()) {
                    in 0..(rootSize.height / 2) -> Alignment.Top
                    else -> Alignment.Bottom
                }
                horizontalAlignment = when (offset.x.roundToInt() + 2) {
                    in 0..((rootSize.width / 2) - (size.width / 2)) -> when (direction) {
                        LayoutDirection.Ltr -> Alignment.Start
                        LayoutDirection.Rtl -> Alignment.End
                    }

                    in ((rootSize.width / 2) + (size.width / 2))..rootSize.width -> when (direction) {
                        LayoutDirection.Ltr -> Alignment.End
                        LayoutDirection.Rtl -> Alignment.Start
                    }

                    else -> Alignment.CenterHorizontally
                }
            },
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
            val density = LocalDensity.current
            val offset = with(density) { DpOffset(offset.x.toDp(), offset.y.toDp()) }
            val expansion = @Composable {
                Box(
                    modifier = Modifier
                        .alpha(state.alpha)
                        .scale(state.scale)
                ) {
                    expansion()
                }
            }
            val top = if (verticalAlignment === Alignment.Bottom) expansion else null
            val bottom = if (verticalAlignment === Alignment.Top) expansion else null
            AnchoredLayout(
                modifier = Modifier
                    .offset(offset.x, offset.y - statusBars.calculateTopPadding()),
                horizontalAlignment = horizontalAlignment,
                top = top,
                bottom = bottom
            ) {
                Box(
                    modifier = Modifier
                        .size(size.toDpSize(density))
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
private fun DialogClonePreview() = PreviewLayout {
    OverlayContainer(modifier = Modifier.fillMaxSize()) {
        val state = rememberDialogCloneState()
        DialogClone(
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
    //DialogClone()
}