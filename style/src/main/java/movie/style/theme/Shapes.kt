package movie.style.theme

import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import movie.style.util.pc

internal val ThemeShapes = Shapes(
    extraSmall = RoundedCornerShape(.5.pc),
    small = RoundedCornerShape(1.pc),
    medium = RoundedCornerShape(1.5.pc),
    large = RoundedCornerShape(2.pc),
    extraLarge = RoundedCornerShape(3.pc),
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