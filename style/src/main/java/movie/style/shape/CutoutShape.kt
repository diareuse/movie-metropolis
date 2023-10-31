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
import movie.style.shape.CutoutShape.Orientation

class CutoutShape(
    private val cornerSize: CornerSize,
    private val orientation: Orientation
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path()
        val cornerRadius = CornerRadius(cornerSize.toPx(size, density))
        path.addRoundRect(
            RoundRect(
                Rect(Offset.Zero, size),
                cornerRadius,
                CornerRadius.Zero,
                cornerRadius,
                cornerRadius
            )
        )

        val topLeft = inverseCorner(cornerRadius)
        path.op(path, topLeft, PathOperation.Union)

        val bottomRight = inverseCorner2(cornerRadius)
        bottomRight.transform(Matrix().apply {
            rotateZ(90f)
            scale(y = -1f)
        })
        bottomRight.translate(Offset(x = size.width, y = size.height))
        path.op(path, bottomRight, PathOperation.Union)

        when (orientation) {
            Orientation.TopLeft -> path.transform(Matrix().apply {
                scale(x = -1f)
                translate(x = -size.width)
            })

            Orientation.TopRight -> {} // noop
            Orientation.BottomRight -> path.transform(Matrix().apply {
                scale(y = -1f)
                translate(y = -size.height)
            })

            Orientation.BottomLeft -> path.transform(Matrix().apply {
                scale(x = -1f, y = -1f)
                translate(x = -size.width, y = -size.height)
            })
        }

        return Outline.Generic(path)
    }

    private fun inverseCorner(radius: CornerRadius) = Path().apply {
        moveTo(0f, 0f)
        lineTo(radius.x, 0f)
        lineTo(0f, radius.y)
        quadraticBezierTo(0f, 0f, 0f, radius.y)
        quadraticBezierTo(0f, 0f, -radius.x, 0f)
        close()
    }

    private fun inverseCorner2(radius: CornerRadius) = Path().apply {
        moveTo(0f, 0f)
        lineTo(-radius.x, 0f)
        lineTo(0f, -radius.y)
        quadraticBezierTo(0f, 0f, 0f, -radius.y)
        quadraticBezierTo(0f, 0f, radius.x, 0f)
        close()
    }

    enum class Orientation {
        TopLeft, TopRight, BottomRight, BottomLeft
    }

}

@Preview(showBackground = true)
@Composable
private fun CutoutShapePreview(
    @PreviewParameter(OrientationProvider::class)
    orientation: Orientation
) {
    val shape = CutoutShape(
        cornerSize = CornerSize(16.dp),
        orientation = orientation
    )
    Box(
        modifier = Modifier
            .padding(16.dp)
            .size(32.dp)
            .background(Color.Blue, shape)
    )
}

private class OrientationProvider : PreviewParameterProvider<Orientation> {
    override val values = Orientation.entries.asSequence()
}