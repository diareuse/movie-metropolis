package movie.metropolis.app.ui.movie.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.hazeEffect
import movie.metropolis.app.screen.listing.component.TextButton
import movie.style.layout.PreviewLayout
import movie.style.util.pc

@Composable
fun FindTicketButton(
    onClick: () -> Unit,
    haze: HazeState,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) = TextButton(
    modifier = modifier
        .shadow(16.dp, shape = CircleShape)
        .clip(CircleShape)
        .hazeEffect(haze),
    onClick = onClick,
    content = {
        Row(
            modifier = Modifier
                .weight(1f)
                .align(Alignment.CenterVertically),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(1.pc, Alignment.CenterHorizontally)
        ) {
            content()
        }
    }
)

@PreviewLightDark
@PreviewFontScale
@Composable
private fun FindTicketButtonPreview() = PreviewLayout {
    FindTicketButton({}, remember { HazeState() }) {
        Text("Button")
    }
}