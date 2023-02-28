package movie.metropolis.app.screen.booking.component

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.tooling.preview.datasource.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout

@Composable
fun TicketMetadata(
    seats: Int,
    hall: @Composable () -> Unit,
    time: @Composable () -> Unit,
    row: @Composable (Int) -> Unit,
    seat: @Composable (Int) -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = modifier,
    verticalArrangement = Arrangement.spacedBy(16.dp)
) {
    val headlineTextStyle = LocalTextStyle.current.copy(
        fontWeight = FontWeight.Bold
    )
    Table(
        rows = 2,
        columns = 2
    ) { row, column ->
        when (row) {
            0 -> when (column) {
                0 -> Text(stringResource(R.string.venue), style = headlineTextStyle)
                1 -> Text(stringResource(R.string.time), style = headlineTextStyle)
            }

            else -> when (column) {
                0 -> hall()
                1 -> time()
            }
        }
    }
    Table(
        rows = seats + 1,
        columns = 2
    ) { row, column ->
        when (row) {
            0 -> when (column) {
                0 -> Text(stringResource(R.string.row), style = headlineTextStyle)
                1 -> Text(stringResource(R.string.seat), style = headlineTextStyle)
            }

            else -> when (column) {
                0 -> row(row - 1)
                1 -> seat(row - 1)
            }
        }
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_NO)
@Composable
private fun TicketMetadataPreviewLight(
    @PreviewParameter(TicketMetadataParameter::class, 1) parameter: TicketMetadataParameter.Data
) = PreviewLayout {
    TicketMetadata(
        seats = parameter.seats.size,
        hall = { Text(parameter.hall) },
        time = { Text(parameter.time) },
        row = { Text(parameter.seats[it].first) },
        seat = { Text(parameter.seats[it].second) }
    )
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun TicketMetadataPreviewDark(
    @PreviewParameter(TicketMetadataParameter::class, 1) parameter: TicketMetadataParameter.Data
) = PreviewLayout {
    TicketMetadata(
        seats = parameter.seats.size,
        hall = { Text(parameter.hall) },
        time = { Text(parameter.time) },
        row = { Text(parameter.seats[it].first) },
        seat = { Text(parameter.seats[it].second) }
    )
}

private class TicketMetadataParameter :
    CollectionPreviewParameterProvider<TicketMetadataParameter.Data>(listOf(Data())) {
    data class Data(
        val hall: String = "IMAX",
        val seats: List<Pair<String, String>> = listOf("13" to "10", "13" to "11"),
        val time: String = "12:34"
    )
}