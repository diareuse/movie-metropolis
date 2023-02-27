package movie.metropolis.app.screen.booking.component

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.*
import androidx.compose.ui.text.style.*
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable

@Composable
fun Table(
    vararg rows: TableRow,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) = Table(
    rows = rows.toList().immutable(),
    modifier = modifier,
    textAlign = textAlign
)

@Composable
fun Table(
    rows: ImmutableList<TableRow>,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) {
    Column(modifier = modifier) {
        for (row in rows) Row {
            for (column in row.columns)
                Text(
                    text = column,
                    modifier = Modifier.weight(1f),
                    style = row.textStyle ?: LocalTextStyle.current,
                    textAlign = textAlign
                )
        }
    }
}

@Composable
fun Table(
    rows: Int,
    columns: Int,
    modifier: Modifier = Modifier,
    content: @Composable (row: Int, column: Int) -> Unit
) {
    Column(modifier = modifier) {
        for (row in 0 until rows) Row {
            for (column in 0 until columns) Box(
                modifier = Modifier.weight(1f)
            ) {
                content(row, column)
            }
        }
    }
}

@Stable
data class TableRow(
    val columns: ImmutableList<String>,
    val textStyle: TextStyle?
) {
    constructor(vararg columns: String, style: TextStyle? = null) : this(
        columns.toList().immutable(), style
    )
}