package movie.style.shape

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.style.util.toIntSize
import movie.style.util.toPath
import movie.style.util.toSize

class CompositeShape private constructor(
    private val baseline: Shape,
    private val shapes: ImmutableList<ShapeDefinition>
) : Shape {

    constructor(
        builder: Builder
    ) : this(
        builder.baseline,
        builder.shapes.toImmutableList()
    )

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline = with(density) {
        val path = Path()
        path.addOutline(baseline.createOutline(size, layoutDirection, density))
        for ((shape, preferredSize, alignment, operation) in shapes) {
            val childSize = preferredSize.takeIf { it.isSpecified }
                ?.run {
                    copy(
                        if (width == Dp.Infinity) size.width.toDp() else width,
                        if (height == Dp.Infinity) size.height.toDp() else height
                    )
                }
                ?.toSize(density) ?: size
            val childPath = shape.createOutline(childSize, layoutDirection, density).toPath()
            val offset = alignment.align(childSize.toIntSize(), size.toIntSize(), layoutDirection)
            childPath.translate(offset.toOffset())
            path.op(path, childPath, operation)
        }
        return Outline.Generic(path)
    }

    @Stable
    class Builder {

        internal var baseline: Shape = RectangleShape
            private set
        private val _shapes = mutableListOf<ShapeDefinition>()
        internal val shapes: List<ShapeDefinition> get() = _shapes

        fun setBaseline(shape: Shape) = apply {
            baseline = shape
        }

        fun addShape(
            shape: Shape,
            size: DpSize = DpSize.Unspecified,
            alignment: Alignment = Alignment.TopStart,
            operation: PathOperation = PathOperation.Intersect
        ) = apply {
            _shapes += ShapeDefinition(shape, size, alignment, operation)
        }

        fun build() = CompositeShape(this)

    }

    internal data class ShapeDefinition(
        val shape: Shape,
        val size: DpSize,
        val alignment: Alignment,
        val operation: PathOperation
    )

    companion object {
        inline fun build(builder: Builder.() -> Unit): CompositeShape {
            return Builder().apply(builder).build()
        }

        @Composable
        inline operator fun invoke(
            key1: Any?,
            crossinline builder: Builder.() -> Unit
        ) = remember(key1) {
            build(builder)
        }

        @Composable
        inline operator fun invoke(
            key1: Any?,
            key2: Any?,
            crossinline builder: Builder.() -> Unit
        ) = remember(key1, key2) {
            build(builder)
        }

        @Composable
        inline operator fun invoke(
            key1: Any?,
            key2: Any?,
            key3: Any?,
            crossinline builder: Builder.() -> Unit
        ) = remember(key1, key2, key3) {
            build(builder)
        }

        @Composable
        inline operator fun invoke(
            vararg keys: Any?,
            crossinline builder: Builder.() -> Unit
        ) = remember(keys = keys) {
            build(builder)
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun CompositeShapePreview() {
    val rect = RoundedCornerShape(16.dp)
    val ticket = TicketShape(
        cutoutSize = 8.dp,
        bottomOffset = 15.dp
    )
    val cutout = CutoutShape(
        cornerSize = CornerSize(16.dp),
        orientation = CutoutShape.Orientation.TopRight
    )
    val shape = CompositeShape {
        setBaseline(rect)
        addShape(ticket)
        addShape(cutout, DpSize(32.dp, 32.dp), Alignment.TopEnd, PathOperation.Difference)
    }
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(128.dp)
            .background(Color.Blue, shape)
    )
}