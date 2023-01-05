package movie.metropolis.app.screen.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import movie.style.haptic.withHaptics
import movie.style.theme.Theme
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

@Suppress("NAME_SHADOWING")
@Composable
fun DatePickerRow(
    start: Date,
    selected: Date,
    onClickDate: (Date) -> Unit,
    modifier: Modifier = Modifier,
    end: Date = Date(start.time + 30.days.inWholeMilliseconds),
    disabledValues: List<Date> = emptyList(),
    formatter: DateFormat = SimpleDateFormat("MMM'\n'd", Locale.getDefault()),
) {
    val start = remember(start) { start.normalize() }
    val dates = remember(start, end) {
        buildList {
            val calendar = Calendar.getInstance()
            calendar.time = start
            do {
                add(calendar.time)
                calendar.add(Calendar.DAY_OF_YEAR, 1)
            } while (calendar.time.before(end))
        }
    }
    val selected = remember(selected) { selected.normalize() }
    val disabledValues = remember(disabledValues) { disabledValues.map { it.normalize() } }
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(dates, key = { it }) {
            DatePickerItem(
                date = formatter.format(it),
                enabled = it !in disabledValues,
                selected = selected == it,
                onClick = { onClickDate(it) }
            )
        }
    }
}

private fun Date.normalize(): Date {
    val calendar = Calendar.getInstance()
    calendar.time = this
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    return calendar.time
}

@Composable
private fun DatePickerItem(
    date: String,
    enabled: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val color = when {
        selected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.surface
    }
    Surface(
        shape = MaterialTheme.shapes.small,
        color = color,
        contentColor = contentColorFor(color).let {
            if (enabled) it else it.copy(alpha = .5f)
        },
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ).takeUnless { selected }
    ) {
        Text(
            modifier = Modifier
                .clickable(enabled = enabled, onClick = onClick.withHaptics())
                .padding(12.dp, 8.dp),
            text = date,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        val now = remember { Date() }
        val disabled = remember { Date(now.time + 3.days.inWholeMilliseconds) }
        DatePickerRow(
            start = now,
            selected = now,
            disabledValues = listOf(disabled),
            onClickDate = {})
    }
}
