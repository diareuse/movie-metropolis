package movie.style.modifier

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

fun Modifier.surface(
    color: Color,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    shadowColor: Color = color
) = shadow(elevation, shape, false, shadowColor, shadowColor)
    .clip(shape)
    .background(color, shape)

fun Modifier.surface(
    brush: Brush,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    shadowColor: Color = Color.Black
) = shadow(elevation, shape, false, shadowColor, shadowColor)
    .clip(shape)
    .background(brush, shape)

fun Modifier.surface(
    tonalElevation: Dp,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    shadowColor: Color = Color.Black
) = composed {
    val elevatedColor = MaterialTheme.colorScheme.surfaceColorAtElevation(tonalElevation)
    surface(elevatedColor, shape, elevation, shadowColor)
}

fun Modifier.surfacePoster(tint: Color = Color.Black) = composed {
    val tintAnimated by animateColorAsState(targetValue = tint)
    surface(
        color = Theme.color.container.background,
        shape = Theme.container.poster,
        elevation = 24.dp,
        shadowColor = tintAnimated
    )
}
