package movie.metropolis.app.ui.home.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.HazeTint
import dev.chrisbanes.haze.hazeEffect
import dev.chrisbanes.haze.hazeSource
import kotlinx.coroutines.launch
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout
import movie.style.util.pc

class TouchResponsiveState {
    var center by mutableStateOf(Offset.Unspecified)
    var position by mutableStateOf(center)
    var size by mutableStateOf(Size.Zero)
    val offsetMagnitudeX by derivedStateOf { if (size.isEmpty()) .5f else position.x / size.width }
}

private fun Modifier.touchResponsive(
    state: TouchResponsiveState,
    shape: Shape? = null,
    contentColor: Color = Color.Unspecified
) = composed {
    val scope = rememberCoroutineScope()
    val cc = contentColor.takeOrElse { LocalContentColor.current }
    val shape = shape ?: MaterialTheme.shapes.large
    pointerInput(Unit) {
        awaitEachGesture {
            val p = awaitFirstDown()
            p.consume()
            val start = p.position
            val job = scope.launch {
                animate(Offset.VectorConverter, state.position, start) { it, _ ->
                    state.position = it
                }
            }
            while (true) {
                val e = awaitPointerEvent()
                job.cancel()
                e.changes.forEach {
                    it.consume()
                    state.position = it.position
                }
                if (e.type == PointerEventType.Release) {
                    scope.launch {
                        animate(Offset.VectorConverter, state.position, state.center) { it, _ ->
                            state.position = it
                        }
                    }
                    break
                }
            }
        }
    }
        .graphicsLayer {
            if (state.position.isUnspecified || state.center.isUnspecified) {
                state.position = size.center
                state.center = state.position
            }
            if (state.size != size) {
                state.size = size
            }
            rotationX = -(180f * (state.position.y - size.height / 2f) / size.height)
                .squeezeInto(-180f..180f, -5f..5f)
            rotationY = (180f * (state.position.x - size.width / 2f) / size.width)
                .squeezeInto(-180f..180f, -5f..5f)
            translationX = (state.position.x - size.width / 2f)
                .squeezeInto(size.width / 2f..-size.width / 2f, -20f..20f)
            translationY = (state.position.y - size.width / 2f)
                .squeezeInto(size.height / 2f..-size.height / 2f, -20f..20f)
        }
        .drawWithCache {
            onDrawWithContent {
                drawContent()
                val offset = state.position.x / size.width
                val brush = Brush.linearGradient(
                    0f to Color.Transparent,
                    offset to cc.copy(.2f),
                    1f to Color.Transparent,
                    start = Offset.Infinite.copy(y = 0f),
                    end = Offset.Infinite.copy(x = 0f)
                )
                val outline = shape.createOutline(size, layoutDirection, this)
                drawOutline(outline, brush, blendMode = BlendMode.Color)
            }
        }
}

fun Float.squeezeInto(
    original: ClosedFloatingPointRange<Float>,
    range: ClosedFloatingPointRange<Float>
): Float {
    val progress = (this - original.start) / (original.endInclusive - original.start)
    return range.start + (range.endInclusive - range.start) * progress
}

@Composable
fun LoyaltyCard(
    haze: HazeState,
    logo: @Composable () -> Unit,
    title: @Composable () -> Unit,
    name: @Composable () -> Unit,
    number: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.large,
    color: Color = MaterialTheme.colorScheme.surface,
    tint: HazeTint = HazeTint(MaterialTheme.colorScheme.surface.copy(.1f)),
    imageHighlightColor: Color = Color(0xffE8873D),
    contentColor: Color = tint.color.copy(1f),
    shadowColor: Color = LocalContentColor.current,
    overlayColor: Color = Color.White,
    state: TouchResponsiveState = remember { TouchResponsiveState() }
) = Box(
    modifier = modifier
        .touchResponsive(state)
        .clip(shape)
        .drawWithContent {
            drawContent()
            drawOutline(
                outline = shape.createOutline(size, layoutDirection, this),
                brush = Brush.linearGradient(
                    listOf(
                        imageHighlightColor.copy(.25f),
                        imageHighlightColor.copy(.05f).compositeOver(contentColor).copy(.25f),
                        imageHighlightColor.copy(0f).compositeOver(contentColor).copy(.25f),
                        Color.Transparent
                    ),
                    start = Offset.Infinite.copy(y = 0f),
                    end = Offset.Infinite.copy(x = 0f)
                ),
                style = Stroke(width = 4.dp.toPx())
            )
        }
        .hazeEffect(
            state = haze,
            style = HazeDefaults.style(
                backgroundColor = color,
                tint = tint,
                blurRadius = 16.dp,
                noiseFactor = 8f
            )
        )
        .aspectRatio(3.37f / 2.125f)
) {
    CompositionLocalProvider(LocalContentColor provides contentColor) {
        ProvideTextStyle(
            MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                shadow = Shadow(
                    shadowColor.copy(1f),
                    Offset(2f, 2f) * -(state.offsetMagnitudeX - .5f),
                    2f
                ),
                brush = Brush.linearGradient(
                    0f to Color.Black.copy(.2f).compositeOver(overlayColor),
                    state.offsetMagnitudeX to overlayColor,
                    1f to Color.Black.copy(.2f).compositeOver(overlayColor),
                    start = Offset.Infinite.copy(y = 0f),
                    end = Offset.Infinite.copy(x = 0f)
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .padding(2.pc)
                    .matchParentSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    title()
                    Box(
                        modifier = Modifier.height(48.dp),
                        propagateMinConstraints = true
                    ) {
                        logo()
                    }
                }
                Column {
                    name()
                    ProvideTextStyle(TextStyle(fontFamily = FontFamily.Monospace)) {
                        number()
                    }
                }
            }
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun LoyaltyCardPreview() = PreviewLayout {
    val state = remember { HazeState() }
    Box(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
        Box(
            modifier = Modifier
                .hazeSource(state)
                .matchParentSize()
                .rotate(45f)
                .background(Color.Blue, MaterialTheme.shapes.large)
        )
        LoyaltyCard(
            modifier = Modifier
                .padding(1.pc)
                .fillMaxWidth(),
            haze = state,
            logo = {
                Image(painterResource(R.drawable.ic_logo_cinemacity), null)
            },
            title = { Text("Premium") },
            name = { Text("John Doe") },
            number = { Text("1234 5678 9012") }
        )
    }
}