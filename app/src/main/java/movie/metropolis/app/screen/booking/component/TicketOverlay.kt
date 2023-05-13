package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.unit.*
import movie.style.layout.CutoutLayout
import movie.style.layout.TicketShape
import movie.style.theme.Theme

@Composable
fun TicketOverlay(
    cutoutOffset: Int,
    overlay: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    content: @Composable BoxScope.() -> Unit
) {
    val density = LocalDensity.current
    val container = Theme.container
    val shape = remember(cutoutOffset, density) {
        TicketShape(container.card.topStart, 16.dp, cutoutOffset, density)
    }
    CutoutLayout(
        modifier = modifier,
        color = color,
        cutoutShape = Theme.container.button,
        contentShape = shape,
        cutoutPadding = 12.dp,
        overlay = overlay
    ) {
        Box {
            content()
        }
    }
}