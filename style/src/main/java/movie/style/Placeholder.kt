@file:Suppress("DEPRECATION")

package movie.style

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme
import movie.style.util.pc

fun Modifier.imagePlaceholder(
    visible: Boolean = true
) = composed {
    imagePlaceholder(
        shape = Theme.container.button,
        visible = visible
    )
}

fun Modifier.imagePlaceholder(
    shape: Shape,
    visible: Boolean = true
) = composed {
    this
}

fun Modifier.textPlaceholder(
    visible: Boolean = true
) = composed {
    textPlaceholder(
        shape = Theme.container.buttonSmall,
        visible = visible
    )
}

fun Modifier.textPlaceholder(
    shape: Shape,
    visible: Boolean = true
) = composed {
    this
}

@Composable
fun TextPlaceholder(
    width: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(25)
) {
    val density = LocalDensity.current
    val style = LocalTextStyle.current
    val color = LocalContentColor.current
    Spacer(
        modifier = modifier
            .padding(vertical = .1.pc)
            .width(width)
            .height(with(density) { style.fontSize.toDp() })
            .background(color, shape)
    )
}

@Composable
fun ContentPlaceholder(
    modifier: Modifier = Modifier,
    alpha: Float = .25f,
    shape: Shape = RectangleShape,
    color: Color = LocalContentColor.current
) {
    Spacer(
        modifier = modifier.background(color.copy(alpha), shape)
    )
}

fun Modifier.backgroundPlaceholder(
    alpha: Float = .1f,
    shape: Shape = RectangleShape,
    color: Color = Color.Unspecified
) = composed {
    background(color.takeOrElse { LocalContentColor.current.copy(alpha) }, shape)
}