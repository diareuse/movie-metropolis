package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import movie.style.layout.PreviewLayout

@Composable
fun SectionTitle(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
) = Box(modifier = modifier.fillMaxWidth(), propagateMinConstraints = true) {
    ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)) {
        text()
    }
}

@Composable
fun ScreenTitle(
    modifier: Modifier = Modifier,
    text: @Composable () -> Unit,
) = Box(modifier = modifier.fillMaxWidth(), propagateMinConstraints = true) {
    ProvideTextStyle(MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)) {
        text()
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun SectionTitlePreview() = PreviewLayout {
    SectionTitle { Text("I am title") }
}