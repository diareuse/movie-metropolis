package movie.metropolis.app.screen.booking.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import movie.style.Image
import movie.style.layout.PreviewLayout
import movie.style.modifier.surface
import movie.style.rememberImageState
import movie.style.theme.Theme

@Composable
fun TicketItem(
    cinema: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    name: @Composable () -> Unit,
    date: @Composable () -> Unit,
    metadata: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    onBottomOffsetChanged: (Int) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            propagateMinConstraints = true
        ) {
            poster()
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { onBottomOffsetChanged(it.size.height) }
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProvideTextStyle(Theme.textStyle.title.copy(textAlign = TextAlign.Center)) {
                name()
            }
            ProvideTextStyle(Theme.textStyle.caption.copy(textAlign = TextAlign.Center)) {
                cinema()
                date()
                metadata()
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun TicketItemPreviewLight(
    @PreviewParameter(TicketItemParameter::class, 1) parameter: TicketItemParameter.Data
) = PreviewLayout {
    TicketItem(
        modifier = Modifier.surface(4.dp),
        cinema = { Text(parameter.cinema) },
        poster = { Image(rememberImageState(url = parameter.poster)) },
        name = { Text(parameter.name) },
        date = { Text(parameter.date) },
        metadata = {
            TicketMetadata(
                modifier = Modifier.padding(top = 16.dp),
                seats = parameter.seats.size,
                hall = { Text(parameter.hall) },
                time = { Text(parameter.time) },
                row = { Text(parameter.seats[it].first) },
                seat = { Text(parameter.seats[it].second) }
            )
        }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TicketItemPreviewDark(
    @PreviewParameter(TicketItemParameter::class, 1) parameter: TicketItemParameter.Data
) = PreviewLayout {
    TicketItem(
        modifier = Modifier.surface(4.dp),
        cinema = { Text(parameter.cinema) },
        poster = { Image(rememberImageState(url = parameter.poster)) },
        name = { Text(parameter.name) },
        date = { Text(parameter.date) },
        metadata = {
            TicketMetadata(
                modifier = Modifier.padding(top = 16.dp),
                seats = parameter.seats.size,
                hall = { Text(parameter.hall) },
                time = { Text(parameter.time) },
                row = { Text(parameter.seats[it].first) },
                seat = { Text(parameter.seats[it].second) }
            )
        }
    )
}

private class TicketItemParameter :
    CollectionPreviewParameterProvider<TicketItemParameter.Data>(listOf(Data())) {
    data class Data(
        val cinema: String = "Cinema City",
        val poster: String = "https://theposterdb.com/api/assets/282262/view",
        val name: String = "Antman & The Wasp: Quantumania",
        val date: String = "15. 1. 2022",
        val time: String = "14:15",
        val hall: String = "IMAX",
        val seats: ImmutableList<Pair<String, String>> = listOf("13" to "10").toImmutableList(),
        val barcode: String = "123456789"
    )
}