package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.EmptyShapeLayout
import movie.style.theme.Theme

@Composable
fun TicketItemError(
    modifier: Modifier = Modifier,
) {
    EmptyShapeLayout(
        modifier = modifier.fillMaxSize(),
        color = Theme.color.container.error,
        contentPadding = PaddingValues(24.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("🥺", style = Theme.textStyle.title.copy(fontSize = 48.sp))
            Text(stringResource(R.string.error_booking), textAlign = TextAlign.Center)
        }
    }
}