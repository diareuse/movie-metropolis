package movie.metropolis.app.screen.setup

import androidx.compose.animation.core.animateIntOffsetAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
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
            nextInt(x.first, x.last - sizePx),
            nextInt(y.first, y.last - sizePx)
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