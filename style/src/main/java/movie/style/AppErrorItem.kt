package movie.style

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.EmptyShapeLayout
import movie.style.theme.Theme

@Composable
fun AppErrorItem(
    modifier: Modifier = Modifier,
    error: String
) {
    EmptyShapeLayout(
        modifier = modifier,
        shape = Theme.container.card,
        color = Theme.color.container.error
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text("🥺", style = Theme.textStyle.title.copy(fontSize = 48.sp))
            Text(error)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        AppErrorItem(error = "Something went wrong")
    }
}