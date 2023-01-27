package movie.metropolis.app.screen.booking.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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

@Stable
data class TableRow(
    val columns: ImmutableList<String>,
    val textStyle: TextStyle?
) {
    constructor(vararg columns: String, style: TextStyle? = null) : this(
        columns.toList().immutable(), style
    )
}