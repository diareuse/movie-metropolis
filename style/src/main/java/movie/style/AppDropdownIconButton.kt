package movie.style

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun AppDropdownIconButton(
    painter: Painter,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    color: Color = LocalContentColor.current,
    items: @Composable ColumnScope.() -> Unit
) {
    Box(modifier = modifier) {
        var isExpanded by rememberSaveable { mutableStateOf(false) }
        AppIconButton(
            painter = painter,
            onClick = {
                onClick()
                isExpanded = !isExpanded
            },
            enabled = enabled,
            color = color
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false },
            content = items
        )
    }
}