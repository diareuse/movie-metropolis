package movie.metropolis.app.screen.card.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.style.layout.PreviewLayout
import kotlin.random.Random.Default.nextInt

fun nextColor(
    alpha: IntRange = 0..0xff,
    red: IntRange = 0..0xff,
    green: IntRange = 0..0xff,
    blue: IntRange = 0..0xff
): Long {
    var out = if (alpha.isEmpty()) 0 else nextInt(alpha.first, alpha.last).toLong()
    out = out shl 8
    out += if (red.isEmpty()) 0 else nextInt(red.first, red.last)
    out = out shl 8
    out += if (green.isEmpty()) 0 else nextInt(green.first, green.last)
    out = out shl 8
    out += if (blue.isEmpty()) 0 else nextInt(blue.first, blue.last)
    return out
}

@Composable
fun ScatterPointBackground(
    bounds: DpRect,
    modifier: Modifier = Modifier,
    count: Int = 20,
) {
    val density = LocalDensity.current
    ScatterPointBackground(
        modifier = modifier,
        bounds = with(density) { bounds.toRect() },
        count = count
    )
}

@Composable
fun ScatterPointBackground(
    bounds: Rect,
    modifier: Modifier = Modifier,
    count: Int = 20,
) = Box(modifier = modifier) {
    val density = LocalDensity.current
    val points = remember(bounds) { ScatterPoint.create(bounds, density, count) }
    for ((offset, color) in points)
        Box(
            modifier = Modifier
                .size(64.dp)
                .offset(offset.x - 32.dp, offset.y - 32.dp)
                .blur(10.dp, BlurredEdgeTreatment.Unbounded)
                .background(color, CircleShape)
        )
}

@Composable
fun CardBackgroundFront(
    modifier: Modifier = Modifier,
) = BoxWithConstraints(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val bounds = remember(maxWidth, maxHeight) {
        val w = maxWidth
        val h = maxHeight
        DpRect(DpOffset.Zero, DpSize(w / 2, h))
    }
    ScatterPointBackground(bounds = bounds)
}

@Composable
fun CardBackgroundBack(
    modifier: Modifier = Modifier,
) = BoxWithConstraints(
    modifier = modifier,
    propagateMinConstraints = true
) {
    val bounds = remember(maxWidth, maxHeight) {
        val w = maxWidth
        val h = maxHeight
        DpRect(DpOffset(0.dp, h / 2), DpSize(w, h / 2))
    }
    ScatterPointBackground(bounds = bounds)
}

data class ScatterPoint(
    val offset: DpOffset,
    val color: Color
) {

    companion object {

        fun create(bounds: Rect, density: Density, count: Int = 27): ImmutableList<ScatterPoint> {
            val out = mutableListOf<ScatterPoint>()
            val bounds = bounds.roundToIntRect()
            with(density) {
                repeat(count) {
                    val x = nextInt(bounds.left, bounds.right)
                    val y = nextInt(bounds.top, bounds.bottom)
                    val color = Color(nextColor(red = 128..255, green = 15..165, blue = 0..-1))
                    out += ScatterPoint(DpOffset(x.toDp(), y.toDp()), color).also {
                    }
                }
            }
            return out.sortedBy { it.color.luminance() }.toImmutableList()
        }

    }

}

@Preview
@Composable
private fun CardBackgroundFrontPreview() = PreviewLayout {
    CardBackgroundFront(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    )
}

@Preview
@Composable
private fun CardBackgroundBackPreview() = PreviewLayout {
    CardBackgroundBack(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1.5f)
    )
}