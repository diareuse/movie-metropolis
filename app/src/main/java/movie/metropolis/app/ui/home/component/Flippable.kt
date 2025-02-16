package movie.metropolis.app.ui.home.component

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresApi
import androidx.compose.animation.SplineBasedFloatDecayAnimationSpec
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.input.pointer.util.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import androidx.core.content.getSystemService
import kotlinx.coroutines.launch
import movie.style.layout.PreviewLayout

private val CardFlipWaveform
    @RequiresApi(Build.VERSION_CODES.Q)
    get() = VibrationEffect.createWaveform(longArrayOf(1), intArrayOf(255 / 10), -1)

@Composable
fun Flippable(
    front: @Composable () -> Unit,
    back: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var rotation by remember { mutableFloatStateOf(0f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures {
                    scope.launch {
                        animate(
                            initialValue = rotation,
                            targetValue = (rotation + 180f) % 360f,
                            animationSpec = tween(300)
                        ) { it, _ ->
                            rotation = it
                        }
                    }
                }
            }
            .pointerInput(Unit) {
                var start = Offset.Zero
                val velocity = VelocityTracker()
                detectDragGestures(
                    onDragStart = { start = it },
                    onDrag = { c, o ->
                        val dragDelta = c.position.y - start.y
                        start = c.position
                        velocity.addPosition(c.uptimeMillis, c.position)

                        val sensitivity = 0.5f
                        rotation = (rotation + dragDelta * sensitivity) % 360
                        if (rotation < 0) {
                            rotation = 360 + rotation
                        }
                    },
                    onDragEnd = {
                        start = Offset.Zero
                        scope.launch {
                            val velocity = velocity.calculateVelocity().y
                            animateDecay(
                                initialValue = rotation,
                                initialVelocity = velocity,
                                animationSpec = SplineBasedFloatDecayAnimationSpec(density)
                            ) { it, _ ->
                                rotation = it % 360
                                if (rotation < 0) {
                                    rotation = 360 + rotation
                                }
                            }
                            val target = if (velocity > 0) {
                                if (rotation >= 180f) 360f else 180f
                            } else {
                                if (rotation <= 180f) 0f else 180f
                            }
                            animate(
                                initialValue = rotation,
                                targetValue = target,
                                animationSpec = tween(300)
                            ) { it, _ ->
                                rotation = it
                            }
                        }
                    },
                    onDragCancel = {
                        start = Offset.Zero
                    }
                )
            },
        propagateMinConstraints = true
    ) {
        /*Box(
            modifier = Modifier.graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f
            },
            propagateMinConstraints = true
        ) {
            if (rotation <= 90 || rotation >= 270) {
                front()
            } else {
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            rotationY = 180f
                        }
                ) {
                    back()
                }
            }
        }*/
        if (rotation <= 90 || rotation >= 270) Box(
            modifier = Modifier.graphicsLayer {
                rotationX = rotation % 360
                cameraDistance = size.maxDimension * 2
            },
            propagateMinConstraints = true
        ) {
            front()
        } else if (rotation in 90f..270f) Box(
            modifier = Modifier.graphicsLayer {
                rotationX = (rotation + 180) % 360
                cameraDistance = size.maxDimension * 2
            },
            propagateMinConstraints = true
        ) {
            back()
            val context = LocalContext.current
            DisposableEffect(context) {
                val v = context.getSystemService<Vibrator>()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    v?.vibrate(CardFlipWaveform)
                } else {
                    v?.vibrate(5)
                }
                onDispose {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        v?.vibrate(CardFlipWaveform)
                    } else {
                        v?.vibrate(5)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun FlippablePreview() = PreviewLayout {
    Flippable(
        modifier = Modifier.size(300.dp, 150.dp),
        front = { Box(Modifier.background(Color.Green)) },
        back = { Box(Modifier.background(Color.Blue)) },
    )
}