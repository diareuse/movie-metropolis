package movie.style

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.*

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