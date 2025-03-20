package movie.metropolis.app.ui.booking.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun DayColumn(
    selected: Boolean,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    haze: HazeState = remember { HazeState() },
    shape: Shape = MaterialTheme.shapes.medium
) {
    val borderSize by animateDpAsState(if (selected) 2.dp else 0.dp)
    val scale by animateFloatAsState(if (selected) 1f else .8f)
    val border =
        Modifier.border(borderSize, MaterialTheme.colorScheme.primary, shape)
    Box(
        modifier = modifier
            .scale(scale)
            .then(if (selected) border else Modifier)
            .clip(shape)
            .hazeEffect(haze)
            .clickable(onClick = onClick)
            .padding(1.pc)
    ) {
        ProvideTextStyle(MaterialTheme.typography.titleMedium.copy(textAlign = TextAlign.Center)) {
            Text(text)
        }
    }
}

@PreviewLightDark
@PreviewFontScale
@Composable
private fun DayColumnPreview() = PreviewLayout {
    Row {
        DayColumn(true, "Wed\n19\nMar", {})
        DayColumn(false, "Wed\n19\nMar", {})
    }
}