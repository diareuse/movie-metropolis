package movie.style

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import movie.style.haptic.withHaptics
import movie.style.theme.Theme

@Composable
fun AppButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    enabled: Boolean = true,
    containerColor: Color = Theme.color.container.primary,
    contentColor: Color = Theme.color.content.primary,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick.withHaptics(),
        enabled = enabled,
        shape = Theme.container.button,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Theme.color.container.surface.copy(alpha = .12f),
            disabledContentColor = Theme.color.container.surface.copy(alpha = .38f),
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = elevation,
            pressedElevation = elevation / 2,
            focusedElevation = elevation / 3 * 2,
            hoveredElevation = elevation * 3 / 2,
            disabledElevation = elevation * 0,
        ),
        content = content
    )
}

@Composable
fun AppIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = LocalContentColor.current,
    content: @Composable () -> Unit
) {
    IconButton(
        onClick = onClick.withHaptics(),
        enabled = enabled,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent,
            contentColor = color,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = Theme.color.container.surface.copy(alpha = .5f),
        ),
        content = content
    )
}

@Composable
fun AppIconButton(
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    color: Color = LocalContentColor.current
) {
    AppIconButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        color = color
    ) {
        Icon(painter, null)
    }
}