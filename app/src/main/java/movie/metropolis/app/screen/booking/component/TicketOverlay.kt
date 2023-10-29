package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.*
import movie.style.layout.CutoutLayout
import movie.style.shape.TicketShape
import movie.style.theme.Theme

@Composable
fun TicketOverlay(
    cutoutOffset: Int,
    overlay: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = remember(cutoutOffset) {
        TicketShape(cutoutSize = 16.dp, bottomOffset = cutoutOffset)
    }
    CutoutLayout(
        color = color,
        cutoutShape = Theme.container.button,
        contentShape = shape,
        modifier = modifier,
        overlay = overlay
    ) {
        Box {
            content()
        }
    }
}