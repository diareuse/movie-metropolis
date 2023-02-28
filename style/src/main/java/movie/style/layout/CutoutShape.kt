package movie.style.layout

import androidx.compose.foundation.shape.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class CutoutShape(
    private val cornerSize: CornerSize,
    private val width: Dp,
    private val height: Dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val radius = CornerRadius(cornerSize.toPx(size, density))

        val primary = Path()
        primary.addRoundRect(RoundRect(0f, 0f, size.width - width.toPx(), size.height, radius))

        val secondary = Path()
        secondary.addRoundRect(RoundRect(0f, height.toPx(), size.width, size.height, radius))

        val helper = Path()
        helper.addRect(
            Rect(
                size.width - width.toPx() * 1.5f,
                height.toPx() / 2,
                size.width - width.toPx() / 2f,
                height.toPx() * 1.5f
            )
        )

        val cutout = Path()
        cutout.addRoundRect(
            RoundRect(
                size.width - width.toPx(),
                0f,
                size.width,
                height.toPx(),
                radius
            )
        )

        val path = Path()
        path.op(primary, secondary, PathOperation.Union)
        path.op(path, helper, PathOperation.Union)
        path.op(path, cutout, PathOperation.Difference)

        return Outline.Generic(path)
    }

    companion object {

        fun from(
            shape: CornerBasedShape,
            width: Dp,
            height: Dp = width
        ) = CutoutShape(
            cornerSize = shape.topStart,
            width = width,
            height = height
        )

    }

}