package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.metropolis.app.screen.detail.plus
import movie.style.layout.EmptyShapeLayout
import movie.style.layout.TicketShape
import movie.style.theme.Theme

@Composable
fun TicketItemEmpty(
    modifier: Modifier = Modifier,
) {
    var height by remember { mutableStateOf(0) }
    EmptyShapeLayout(
        modifier = modifier.onPlaced { height = it.size.height / 4 },
        contentPadding = PaddingValues(24.dp) + PaddingValues(bottom = with(LocalDensity.current) { height.toDp() }),
        shape = TicketShape(
            cornerSize = Theme.container.card.topStart,
            cutoutSize = 16.dp,
            bottomOffset = height,
            density = LocalDensity.current
        )
    ) {
        val tint = Theme.color.container.primary
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painterResource(id = R.drawable.ic_ticket), null, tint = tint)
            Text(stringResource(R.string.empty_booking_active), textAlign = TextAlign.Center)
            Icon(painterResource(id = R.drawable.ic_share), null, tint = tint)
            Text(stringResource(R.string.empty_booking_active_share), textAlign = TextAlign.Center)
        }
    }
}