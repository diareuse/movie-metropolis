package ui.style.shape

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*

class TicketShape(
    private val cutoutSize: Dp,
    private val bottomOffset: Int
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val rect = Rect(Offset.Zero, size)
        path.addRect(rect)
        if (bottomOffset.toFloat() !in 0f..<size.height) {
            return Outline.Generic(path)
        }

        val radius = with(density) { cutoutSize.toPx() }
        val cutoutLeft = getCutout(x = 0f, y = size.height - bottomOffset, radius = radius)
        path.op(path, cutoutLeft, PathOperation.Difference)

        val cutoutRight = getCutout(x = size.width, y = size.height - bottomOffset, radius = radius)
        path.op(path, cutoutRight, PathOperation.Difference)

        for (line in getDashedLine(size, size.height - bottomOffset))
            path.op(path, line, PathOperation.Difference)

        return Outline.Generic(path)
    }

    private fun getCutout(
        x: Float,
        y: Float,
        radius: Float
    ): Path {
        val cutout = Path()
        val boundary = Rect(
            center = Offset(x = x, y = y),
            radius = radius
        )
        cutout.addOval(boundary)
        return cutout
    }

    private fun getDashedLine(size: Size, y: Float, count: Int = 12): Iterable<Path> {
        fun getLine(x: Float): Path {
            val path = Path()
            val height = size.width / count / 5
            val rect = Rect(Offset(x, y - (height / 2)), Size(size.width / count, height))
            val roundRect = RoundRect(rect, CornerRadius(height / 2))
            path.addRoundRect(roundRect)
            return path
        }

        val preferredSize = size.width / count
        return (0 until count)
            .asSequence()
            .filterIndexed { index, _ -> index % 2 == 0 }
            .map { getLine((it * preferredSize) + preferredSize / 2) }
            .asIterable()
    }

}

@Preview(showBackground = true)
@Composable
private fun TicketShapePreview() {
    val shape = TicketShape(
        cutoutSize = 8.dp,
        bottomOffset = 150
    )
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(128.dp)
            .background(Color.Blue, shape)
    )
}