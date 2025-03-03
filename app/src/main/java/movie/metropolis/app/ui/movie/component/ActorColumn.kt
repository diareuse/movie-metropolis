package movie.metropolis.app.ui.movie.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun ActorColumn(
    icon: @Composable () -> Unit,
    name: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier.width(64.dp),
    verticalArrangement = Arrangement.spacedBy(1.pc),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Surface(
        modifier = Modifier.size(48.dp),
        shape = CircleShape
    ) {
        icon()
    }
    ProvideTextStyle(MaterialTheme.typography.labelSmall.copy(textAlign = TextAlign.Center)) {
        name()
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun ActorColumnPreview() = PreviewLayout {
    ActorColumn(
        icon = { Box(Modifier.background(Color.Blue)) },
        name = { Text("Já jsem herec") }
    )
}