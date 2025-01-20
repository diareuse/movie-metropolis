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

private fun Modifier.touchResponsive() = composed {
    var center by remember { mutableStateOf(Offset.Unspecified) }
    var position by remember { mutableStateOf(center) }
    val scope = rememberCoroutineScope()
    pointerInput(Unit) {
        awaitEachGesture {
            val p = awaitFirstDown()
            val start = p.position
            val job = scope.launch {
                animate(Offset.VectorConverter, position, start) { it, _ ->
                    position = it
                }
            }
            while (true) {
                val e = awaitPointerEvent()
                job.cancel()
                e.changes.forEach {
                    position = it.position
                }
                if (e.type == PointerEventType.Release) {
                    scope.launch {
                        animate(Offset.VectorConverter, position, center) { it, _ ->
                            position = it
                        }
                    }
                    break
                }
            }
        }
    }.graphicsLayer {
        if (position.isUnspecified || center.isUnspecified) {
            position = size.center
            center = position
        }
        rotationX = -(180f * (position.y - size.height / 2f) / size.height)
            .squeezeInto(-180f..180f, -5f..5f)
        rotationY = (180f * (position.x - size.width / 2f) / size.width)
            .squeezeInto(-180f..180f, -5f..5f)
        translationX =
            (position.x - size.width / 2f).squeezeInto(size.width / 2f..-size.width / 2f, -20f..20f)
        translationY = (position.y - size.width / 2f).squeezeInto(
            size.height / 2f..-size.height / 2f,
            -20f..20f
        )
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
    tint: HazeTint = HazeTint(MaterialTheme.colorScheme.surface.copy(.1f)),//HazeDefaults.tint(color),
    contentColor: Color = tint.color.copy(1f)
) = Box(
    modifier = modifier
        .touchResponsive()
        .clip(shape)
        .drawWithContent {
            drawContent()
            val outline = shape.createOutline(size, layoutDirection, this)
            val bm = tint.blendMode
            val tint = tint.color
            drawOutline(
                outline = outline,
                brush = Brush.linearGradient(
                    listOf(
                        tint.copy(.8f),
                        tint.copy(.05f),
                        Color.Transparent
                    )
                ),
                style = Stroke(width = 2.dp.toPx()),
                blendMode = bm
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
        ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) {
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