package movie.style.layout

import androidx.compose.foundation.shape.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class CutoutShape(
    private val cardSize: CornerSize,
    private val cornerSize: CornerSize,
    private val width: Dp,
    private val height: Dp
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val radiusCard = CornerRadius(cardSize.toPx(size, density))
        val radiusCutout = CornerRadius(cornerSize.toPx(size, density))

        val primary = Path()
        primary.addRoundRect(RoundRect(0f, 0f, size.width - width.toPx(), size.height, radiusCard))

        val secondary = Path()
        secondary.addRoundRect(RoundRect(0f, height.toPx(), size.width, size.height, radiusCard))

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
                radiusCutout
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
            cardShape: CornerBasedShape,
            cutoutShape: CornerBasedShape,
            width: Dp,
            height: Dp = width
        ) = CutoutShape(
            cardSize = cardShape.topStart,
            cornerSize = cutoutShape.topStart,
            width = width,
            height = height
        )

    }

}