package movie.metropolis.app.screen.booking.component

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay
import movie.metropolis.app.R
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.preview.TicketViewPreview
import movie.style.layout.PreviewWearLayout
import movie.style.modifier.surface
import movie.style.theme.Theme

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun BookingItem(
    name: @Composable () -> Unit,
    date: @Composable () -> Unit,
    time: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(isVisible) {
        if (isVisible) delay(5000)
        isVisible = false
    }
    CompositionLocalProvider(LocalContentColor provides Theme.color.content.surface) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .surface(2.dp, Theme.container.button)
                .combinedClickable(onClick = onClick, onLongClick = { isVisible = true })
                .padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                ProvideTextStyle(Theme.textStyle.emphasis.copy(fontWeight = FontWeight.Medium)) {
                    name()
                }
                AnimatedVisibility(isVisible) {
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(.8f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ProvideTextStyle(Theme.textStyle.caption) {
                            date()
                            Text("•")
                            time()
                        }
                    }
                }
            }
            Icon(
                painter = painterResource(R.drawable.ic_right),
                contentDescription = null,
                tint = LocalContentColor.current
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(TicketViewPreview::class, 2) view: TicketView
) = PreviewWearLayout {
    BookingItem(
        name = { Text(view.name) },
        date = { Text(view.date) },
        time = { Text(view.time) },
        onClick = {}
    )
}