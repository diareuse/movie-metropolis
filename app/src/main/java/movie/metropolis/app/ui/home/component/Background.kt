package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout
import kotlin.math.hypot

fun Modifier.windowBackground() = drawWithCache {
    val red = Color(0xFFFF0800)
    val pink = Color(0xFFF01FFF)
    val purple = Color(0xFF7700FF)
    val transparent = Color.Transparent
    val b1 = Brush.radialGradient(
        colors = listOf(
            pink.copy(.125f),
            purple.copy(.025f)
        ),
        radius = hypot(size.height * .34f, size.width),
        center = Offset.Zero
    )
    val b2 = Brush.radialGradient(
        colors = listOf(red.copy(.05f), transparent),
        center = Offset(size.width, size.height / 2),
        radius = size.maxDimension / 3
    )
    val b3 = Brush.radialGradient(
        colors = listOf(purple.copy(.1f), transparent),
        center = Offset(size.width, size.height),
        radius = size.maxDimension / 2
    )
    onDrawWithContent {
        drawRect(b1)
        drawRect(b2)
        drawRect(b3)
        drawContent()
    }
}

@PreviewLightDark
@Composable
private fun BackgroundPreview() = PreviewLayout {
    Surface {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowBackground()
        )
    }
}