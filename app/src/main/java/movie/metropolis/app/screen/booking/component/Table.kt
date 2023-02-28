package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*

@Composable
fun Table(
    rows: Int,
    columns: Int,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable (row: Int, column: Int) -> Unit
) {
    Column(modifier = modifier) {
        for (row in 0 until rows) Row {
            for (column in 0 until columns) Box(
                modifier = Modifier.weight(1f),
                contentAlignment = contentAlignment
            ) {
                content(row, column)
            }
        }
    }
}
