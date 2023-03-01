package movie.style.layout

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@Composable
fun PreviewLayout(
    padding: PaddingValues = PaddingValues(24.dp),
    content: @Composable () -> Unit
) = Theme {
    Box(modifier = Modifier.padding(padding)) {
        content()
    }
}

@Composable
fun PreviewWearLayout(
    padding: PaddingValues = PaddingValues(16.dp),
    content: @Composable () -> Unit
) = Theme(darkTheme = true) {
    Box(
        modifier = Modifier.padding(padding),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}