package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.model.TicketView
import movie.metropolis.app.model.preview.TicketViewPreview
import movie.style.layout.PreviewWearLayout
import movie.style.theme.Theme

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookingItem(
    name: @Composable () -> Unit,
    date: @Composable () -> Unit,
    time: @Composable () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = Theme.container.button,
            tonalElevation = 2.dp,
            onClick = onClick
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.Center) {
                    ProvideTextStyle(Theme.textStyle.body.copy(fontWeight = FontWeight.Medium)) {
                        name()
                    }
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .alpha(.7f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        ProvideTextStyle(Theme.textStyle.caption.copy(fontSize = 10.sp)) {
                            date()
                            Text("•")
                            time()
                        }
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.WEAR_OS_LARGE_ROUND, showSystemUi = true, showBackground = true)
@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true, showBackground = true)
@Composable
private fun Preview(
    @PreviewParameter(TicketViewPreview::class, 1) view: TicketView
) = PreviewWearLayout {
    BookingItem(
        name = { Text(view.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        date = { Text(view.date) },
        time = { Text(view.time) },
        onClick = {}
    )
}