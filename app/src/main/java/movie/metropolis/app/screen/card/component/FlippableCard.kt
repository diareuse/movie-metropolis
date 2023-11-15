package movie.metropolis.app.screen.card.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.compose.ui.util.*
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.overlay
import movie.style.modifier.surface
import movie.style.modifier.vertical
import movie.style.theme.Theme

private const val CardAspectRatio = 1.5857725f
private const val AxisZRotation = 25f

@Composable
fun FlippableCard(
    rotation: Float,
    container: @Composable (@Composable () -> Unit) -> Unit,
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    key: Any? = null,
) = Box(
    modifier = modifier
        .aspectRatio(CardAspectRatio),
    propagateMinConstraints = true
) {
    val rotation = rotation % 361
    val fraction = (rotation % 90) / 90f
    val rotationZFront = lerp(0f, AxisZRotation, fraction)
    val rotationZBack = lerp(-AxisZRotation, 0f, fraction)
    if (rotation in 0f..<90f || rotation in 270f..360f) key(key, "front") {
        Box(
            modifier = Modifier.graphicsLayer(
                rotationX = rotation % 360f,
                rotationZ = if (rotation % 360 < 180f) rotationZFront else rotationZBack,
                cameraDistance = 1000f
            ),
            propagateMinConstraints = true
        ) {
            container {
                val overlay =
                    if (rotation % 360 < 180f) Color.Transparent
                    else lerp(Color.Black.copy(.5f), Color.Transparent, fraction)
                Box(
                    modifier = Modifier.overlay(overlay, overlay),
                    propagateMinConstraints = true
                ) {
                    front()
                }
            }
        }
    }
    if (rotation in 90f..<270f) key(key, "back") {
        Box(
            modifier = Modifier.graphicsLayer(
                rotationX = (rotation + 180f) % 360f,
                rotationZ = if (rotation >= 180f) rotationZFront else rotationZBack,
                cameraDistance = 1000f
            ),
            propagateMinConstraints = true
        ) {
            container {
                val overlay =
                    if (rotation >= 180f) Color.Transparent
                    else lerp(Color.Black.copy(.5f), Color.Transparent, fraction)
                Box(
                    modifier = Modifier.overlay(overlay, overlay),
                    propagateMinConstraints = true
                ) {
                    back()
                }
            }
        }
    }
}

@Preview
@Composable
private fun FlippableCardPreview() = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    val transition = rememberInfiniteTransition()
    val rotationY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(3000), RepeatMode.Reverse)
    )
    FlippableCard(
        modifier = Modifier
            .fillMaxWidth()
            .vertical(),
        rotation = rotationY,
        front = { Text("Front") },
        back = { Text("Back") },
        container = {
            Box(
                modifier = Modifier
                    .surface(Color.Gray, Theme.container.card, 16.dp, Color.Gray)
                    .glow(Theme.container.card),
                Alignment.Center
            ) {
                it()
            }
        }
    )
}