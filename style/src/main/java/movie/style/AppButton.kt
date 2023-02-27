package movie.style

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.*
import androidx.compose.ui.unit.*
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
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    Button(
        modifier = modifier,
        onClick = onClick.withHaptics(),
        enabled = enabled,
        shape = Theme.container.button,
        contentPadding = contentPadding,
        colors = ButtonDefaults.elevatedButtonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = Theme.color.content.surface.copy(alpha = .12f),
            disabledContentColor = Theme.color.content.surface.copy(alpha = .38f),
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
            disabledContentColor = Theme.color.content.surface.copy(alpha = .5f),
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