package ui.style

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import ui.style.theme.Theme

@Composable
fun PreviewLayout(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) = Theme {
    Box(modifier = modifier) {
        content()
    }
}