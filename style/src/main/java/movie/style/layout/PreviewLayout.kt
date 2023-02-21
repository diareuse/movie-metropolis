package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@Composable
fun PreviewLayout(
    content: @Composable () -> Unit
) = Theme {
    Box(modifier = Modifier.padding(24.dp)) {
        content()
    }
}

@Composable
fun PreviewWearLayout(
    content: @Composable () -> Unit
) = Theme(darkTheme = true) {
    Box(
        modifier = Modifier.padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}