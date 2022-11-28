package movie.metropolis.app.screen.booking

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun Table(
    vararg rows: TableRow,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null
) = Table(
    rows = rows.toList(),
    modifier = modifier,
    textAlign = textAlign
)

@Composable
fun Table(
    rows: List<TableRow>,
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

data class TableRow(
    val columns: List<String>,
    val textStyle: TextStyle?
) {
    constructor(vararg columns: String, style: TextStyle? = null) : this(columns.toList(), style)
}