package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import movie.style.AppImage
import movie.style.textPlaceholder

@Composable
fun TicketItemLoading(
    modifier: Modifier = Modifier,
) {
    var cutoutOffset by remember { mutableStateOf(0) }
    TicketOverlay(
        modifier = modifier,
        cutoutOffset = cutoutOffset,
        overlay = {}
    ) {
        TicketItem(
            cinema = { Text("#".repeat(24), Modifier.textPlaceholder(true)) },
            poster = { AppImage(url = null, Modifier.fillMaxSize()) },
            name = { Text("#".repeat(30), Modifier.textPlaceholder(true)) },
            date = { Text("#".repeat(12), Modifier.textPlaceholder(true)) },
            metadata = {
                TicketMetadata(
                    seats = 1,
                    hall = { Text("#".repeat(6), Modifier.textPlaceholder(true)) },
                    time = { Text("#".repeat(5), Modifier.textPlaceholder(true)) },
                    row = { Text("#".repeat(2), Modifier.textPlaceholder(true)) },
                    seat = { Text("#".repeat(2), Modifier.textPlaceholder(true)) }
                )
            },
            onBottomOffsetChanged = { cutoutOffset = it }
        )
    }
}