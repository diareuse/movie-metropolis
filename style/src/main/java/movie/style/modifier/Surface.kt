package movie.style.modifier

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme

fun Modifier.surface(
    color: Color,
    shape: Shape = RectangleShape,
    elevation: Dp = 0.dp,
    shadowColor: Color = Color.Black
) = shadow(elevation, shape, false, shadowColor, shadowColor)
    .background(color, shape)
    .clip(shape)

fun Modifier.surfacePoster(tint: Color = Color.Black) = composed {
    surface(
        color = Theme.color.container.background,
        shape = Theme.container.poster,
        elevation = 24.dp,
        shadowColor = tint
    )
}
