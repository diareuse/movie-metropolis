package movie.style.layout

import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*

class IntersectShape(
    private val base: Shape,
    private val overlay: Shape
) : Shape {

    private val Outline.path
        get() = when (this) {
            is Outline.Generic -> path
            is Outline.Rectangle -> Path().apply {
                addRect(bounds)
            }

            is Outline.Rounded -> Path().apply {
                addRoundRect(roundRect)
            }
        }

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val baseShape = base.createOutline(size, layoutDirection, density)
        val overlayShape = overlay.createOutline(size, layoutDirection, density)
        val output = Path.combine(PathOperation.Intersect, baseShape.path, overlayShape.path)
        return Outline.Generic(output)
    }

}