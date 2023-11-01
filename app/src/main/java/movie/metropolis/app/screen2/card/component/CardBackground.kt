package movie.metropolis.app.screen2.card.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
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
fun CardBackgroundFront(
    modifier: Modifier = Modifier,
) = BoxWithConstraints(
    modifier = modifier,
    contentAlignment = Alignment.CenterStart
) {
    val bounds = remember(maxWidth, maxHeight) {
        val w = maxWidth.value.toInt()
        val h = maxHeight.value.toInt()
        IntRect(IntOffset(w / -5, h / -2), IntSize(w / 2, h + 2 * h / 5))
    }
    val points = remember(bounds) { ScatterPoint.create(bounds) }
    for ((offset, color) in points)
        Box(
            modifier = Modifier
                .size(64.dp)
                .offset(offset.x, offset.y)
                .blur(10.dp, BlurredEdgeTreatment.Unbounded)
                .background(color, CircleShape)
        )
}

@Composable
fun CardBackgroundBack(
    modifier: Modifier = Modifier,
) = BoxWithConstraints(
    modifier = modifier,
    contentAlignment = Alignment.CenterStart
) {
    val bounds = remember(maxWidth, maxHeight) {
        val w = maxWidth.value.toInt()
        val h = maxHeight.value.toInt()
        IntRect(IntOffset(w / -5, 0), IntSize(w + 2 * w / 5, h / 2))
    }
    val points = remember(bounds) { ScatterPoint.create(bounds) }
    for ((offset, color) in points)
        Box(
            modifier = Modifier
                .size(64.dp)
                .offset(offset.x, offset.y)
                .blur(10.dp, BlurredEdgeTreatment.Unbounded)
                .background(color, CircleShape)
        )
}

data class ScatterPoint(
    val offset: DpOffset,
    val color: Color
) {

    companion object {

        fun create(bounds: IntRect, count: Int = 27): ImmutableList<ScatterPoint> {
            val out = mutableListOf<ScatterPoint>()
            repeat(count) {
                val x = nextInt(bounds.left, bounds.right)
                val y = nextInt(bounds.top, bounds.bottom)
                val color = Color(nextColor(red = 128..255, green = 15..165, blue = 0..-1))
                out += ScatterPoint(DpOffset(x.dp, y.dp), color)
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