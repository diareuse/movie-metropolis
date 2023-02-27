package movie.style.layout

import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
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

    companion object {

        @Composable
        operator fun invoke(cornerSize: CornerSize) = TicketShape(
            bottomOffset = 0,
            density = LocalDensity.current,
            cutoutSize = 16.dp,
            topStart = cornerSize,
            topEnd = cornerSize,
            bottomEnd = cornerSize,
            bottomStart = cornerSize
        )

    }

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
}