package movie.metropolis.app.screen.booking.component

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.ui.*

@Composable
fun TicketFlippable(
    isVisible: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = slideInVertically { it },
        exit = slideOutVertically { it }
    ) {
        content()
    }
}