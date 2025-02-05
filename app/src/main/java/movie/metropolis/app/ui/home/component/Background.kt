package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import kotlin.math.hypot

fun Modifier.windowBackground(background: Color = Color.Black) = drawWithCache {
    onDrawWithContent {
        Color(0xFFFF0800)
        val pink = Color(0xFFF01FFF)
        val purple = Color(0xFF7700FF)
        Color.White
        val transparent = Color.Transparent
        val b1 = Brush.radialGradient(
            listOf(
                pink.copy(.125f),
                purple.copy(.025f)
            ), radius = hypot(size.height * .34f, size.width), center = Offset.Zero
        )
        val b3 = Brush.radialGradient(
            listOf(purple.copy(.1f), transparent),
            center = Offset(size.width, size.height),
            radius = size.maxDimension / 2
        )

        drawRect(b1)
        //drawRect(b2)
        drawRect(b3)
        drawContent()
    }
}

@Preview
@Composable
private fun BackgroundPreview() = PreviewLayout {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowBackground()
    )
}