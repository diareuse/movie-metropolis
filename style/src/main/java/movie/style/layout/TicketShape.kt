package movie.style.layout

import androidx.compose.foundation.shape.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class TicketShape(
    private val cornerSize: CornerSize
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radius = CornerRadius(cornerSize.toPx(size, density))
        val path = Path()
        path.addRoundRect(RoundRect(0f, 0f, size.width, size.height, radius))
        val cutoutLeft = Path()
        cutoutLeft.addOval(
            Rect(
                center = Offset(0f, size.height - size.width),
                radius.x
            )
        )
        val cutoutRight = Path()
        cutoutRight.addOval(
            Rect(
                center = Offset(
                    size.width,
                    size.height - size.width
                ), radius.x
            )
        )
        path.op(path, cutoutLeft, PathOperation.Difference)
        path.op(path, cutoutRight, PathOperation.Difference)
        return Outline.Generic(path)
    }
}