package movie.style.layout

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection

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