package movie.style.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import movie.style.theme.Theme

@Composable
fun PreviewLayout(
    content: @Composable () -> Unit
) = Theme {
    Box(modifier = Modifier.padding(24.dp)) {
        content()
    }
}