package movie.style.modifier

import androidx.compose.animation.core.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*

@Composable
fun Modifier.animateItemAppearance(
    scale: Float = .8f,
    scaleAnimationSpec: AnimationSpec<Float> = tween(600)
): Modifier {
    val isEditMode = LocalView.current.isInEditMode
    var scale by remember { mutableFloatStateOf(if (isEditMode) 1f else scale) }
    LaunchedEffect(Unit) {
        animate(
            initialValue = scale,
            targetValue = 1f,
            animationSpec = scaleAnimationSpec,
            block = { value, _ -> scale = value }
        )
    }
    return this.graphicsLayer {
        alpha = scale
        scaleX = scale
        scaleY = scale
    }
}