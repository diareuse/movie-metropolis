package movie.style.theme

import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*

internal val ThemeShapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(32.dp),
    extraLarge = RoundedCornerShape(0.dp),
)

@Composable
fun CornerBasedShape.extendBy(padding: Dp): CornerBasedShape {
    return extendBy(padding, LocalDensity.current)
}

fun CornerBasedShape.extendBy(padding: Dp, density: Density) = copy(
    topStart = CornerSize(topStart.toDp(density) + padding),
    topEnd = CornerSize(topEnd.toDp(density) + padding),
    bottomEnd = CornerSize(bottomEnd.toDp(density) + padding),
    bottomStart = CornerSize(bottomStart.toDp(density) + padding)
)

private fun CornerSize.toDp(density: Density): Dp {
    return with(density) { toPx(Size.Unspecified, this).toDp() }
}