package movie.metropolis.app.screen.setup.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.theme.Theme
import kotlin.random.Random.Default.nextInt

@Composable
fun Background(
    modifier: Modifier = Modifier,
    count: Int = 3,
    blueprint: @Composable (index: Int) -> Unit
) {
    val density = LocalDensity.current
    val config = LocalConfiguration.current
    val xRange = remember(config, density) {
        0..with(density) { config.screenWidthDp.dp.roundToPx() }
    }
    val yRange = remember(config, density) {
        0..with(density) { config.screenHeightDp.dp.roundToPx() }
    }
    val states = remember(count) {
        List(count) { AnimationState(xRange, yRange) }
    }
    Box(modifier) {
        for ((index, state) in states.withIndex())
            AnimatedBackgroundItem(
                state = state,
                content = { blueprint(index) },
                modifier = Modifier.size(state.size.dp)
            )
    }
}

@Composable
private fun AnimatedBackgroundItem(
    state: AnimationState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val targetOffset = state.targetOffset
    val density = LocalDensity.current
    val offset by animateIntOffsetAsState(
        targetValue = targetOffset,
        animationSpec = tween(2000)
    ) {
        state.updateOffset(density)
    }

    LaunchedEffect(Unit) {
        state.updateOffset(density)
    }

    Box(
        modifier = modifier.offset { offset }
    ) {
        content()
    }
}

@Stable
private class AnimationState(
    private val x: IntRange,
    private val y: IntRange
) {

    var targetOffset: IntOffset by mutableStateOf(
        IntOffset(
            nextInt(x.first, x.last),
            nextInt(y.first, y.last)
        )
    )
    val size = nextInt(50, 300)

    fun updateOffset(density: Density) {
        val sizePx = with(density) { size.dp.roundToPx() }
        targetOffset = IntOffset(
            nextInt(x.first - sizePx, x.last),
            nextInt(y.first - sizePx, y.last)
        )
    }

}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        Background {
            Image(
                painter = painterResource(R.drawable.ic_share),
                contentDescription = null,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}