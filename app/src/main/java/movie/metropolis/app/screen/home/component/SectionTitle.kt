package movie.metropolis.app.screen.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.*
import movie.style.theme.Theme

@Composable
fun SectionTitle(
    name: String,
    modifier: Modifier = Modifier
) {
    Text(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .then(modifier),
        text = name,
        style = Theme.textStyle.title
    )
}