package movie.style.layout

import androidx.compose.foundation.shape.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class TicketShape(
    private val cutoutSize: Dp,
    private val bottomOffset: Int,
    private val density: Density,
    topStart: CornerSize,
    topEnd: CornerSize,
    bottomEnd: CornerSize,
    bottomStart: CornerSize
) : CornerBasedShape(topStart, topEnd, bottomEnd, bottomStart) {

    constructor(
        cornerSize: CornerSize,
        cutoutSize: Dp,
        bottomOffset: Int,
        density: Density
    ) : this(
        bottomOffset = bottomOffset,
        density = density,
        cutoutSize = cutoutSize,
        topStart = cornerSize,
        topEnd = cornerSize,
        bottomEnd = cornerSize,
        bottomStart = cornerSize
    )

    private val fallback by lazy { RoundedCornerShape(topStart, topEnd, bottomEnd, bottomStart) }

    override fun copy(
        topStart: CornerSize,
        topEnd: CornerSize,
        bottomEnd: CornerSize,
        bottomStart: CornerSize
    ): CornerBasedShape {
        return TicketShape(
            cutoutSize,
            bottomOffset,
            density,
            topStart,
            topEnd,
            bottomEnd,
            bottomStart
        )
    }

    override fun createOutline(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float,
        layoutDirection: LayoutDirection
    ): Outline {
        if (bottomOffset <= 0) {
            return fallback.createOutline(size, layoutDirection, density)
        }

        val radius = with(density) { cutoutSize.toPx() }
        val shape = getBaselineShape(size, topStart, topEnd, bottomEnd, bottomStart)
        val cutoutLeft = getCutout(x = 0f, y = size.height - bottomOffset, radius = radius)
        val cutoutRight = getCutout(x = size.width, y = size.height - bottomOffset, radius = radius)
        shape.op(shape, cutoutLeft, PathOperation.Difference)
        shape.op(shape, cutoutRight, PathOperation.Difference)
        for (line in getDashedLine(size, size.height - bottomOffset))
            shape.op(shape, line, PathOperation.Difference)
        return Outline.Generic(shape)
    }

    private fun getBaselineShape(
        size: Size,
        topStart: Float,
        topEnd: Float,
        bottomEnd: Float,
        bottomStart: Float
    ): Path {
        val path = Path()
        val boundary = RoundRect(
            rect = Rect(Offset.Zero, size),
            topLeft = CornerRadius(topStart),
            topRight = CornerRadius(topEnd),
            bottomRight = CornerRadius(bottomEnd),
            bottomLeft = CornerRadius(bottomStart)
        )
        path.addRoundRect(boundary)
        return path
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