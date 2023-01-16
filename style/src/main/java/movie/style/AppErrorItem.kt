package movie.style

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import movie.style.theme.Theme

@Composable
fun AppErrorItem(
    modifier: Modifier = Modifier,
    error: String
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("🥺", style = Theme.textStyle.title.copy(fontSize = 48.sp))
        Text(error)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        AppErrorItem(error = "Something went wrong")
    }
}