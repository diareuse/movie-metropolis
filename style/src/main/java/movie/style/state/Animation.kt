package movie.style.state

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

@Composable
fun Float.animate() = animateFloatAsState(targetValue = this)

@Composable
fun Color.animate() = animateColorAsState(targetValue = this)

@Composable
fun Dp.animate() = animateDpAsState(targetValue = this)

@Composable
fun Dp.smartAnimate(): State<Dp> {
    val initial = remember { this }
    val state = remember { mutableStateOf(this) }
    val nextValue = this
    LaunchedEffect(nextValue) {
        if (state.value == initial) {
            state.value = nextValue
            return@LaunchedEffect
        }
        animate(
            typeConverter = Dp.VectorConverter,
            initialValue = state.value,
            targetValue = nextValue,
            animationSpec = spring(visibilityThreshold = Dp.VisibilityThreshold)
        ) { value, _ ->
            state.value = value
        }
    }
    return state
}