package movie.style.modifier

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.launch
import movie.style.util.rememberSaveable

@Composable
fun Modifier.animateItemAppearance(
    offset: DpOffset = DpOffset(x = 0.dp, 48.dp),
    scale: Float = .8f,
    offsetAnimationSpec: AnimationSpec<DpOffset> = spring(
        Spring.DampingRatioMediumBouncy,
        Spring.StiffnessLow
    ),
    scaleAnimationSpec: AnimationSpec<Float> = tween()
): Modifier {
    val isEditMode = LocalView.current.isInEditMode
    var offset by rememberSaveable(if (isEditMode) DpOffset.Zero else offset)
    var scale by androidx.compose.runtime.saveable.rememberSaveable { mutableFloatStateOf(if (isEditMode) 1f else scale) }
    LaunchedEffect(Unit) {
        launch {
            animate(
                typeConverter = DpOffset.VectorConverter,
                initialValue = offset,
                targetValue = DpOffset.Zero,
                animationSpec = offsetAnimationSpec,
                block = { value, _ -> offset = value }
            )
        }
        launch {
            animate(
                initialValue = scale,
                targetValue = 1f,
                animationSpec = scaleAnimationSpec,
                block = { value, _ -> scale = value }
            )
        }
    }
    return this
        .offset { IntOffset(offset.x.roundToPx(), offset.y.roundToPx()) }
        .scale(scale)
}