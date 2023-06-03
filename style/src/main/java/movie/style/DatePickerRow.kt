package movie.style

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.haptic.withHaptics
import movie.style.theme.Theme
import movie.style.theme.contentColorFor
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.time.Duration.Companion.days

@Suppress("NAME_SHADOWING")
@Composable
fun DatePickerRow(
    selected: Date,
    onClickDate: (Date) -> Unit,
    modifier: Modifier = Modifier,
    start: Date = remember { Date() },
    end: Date = Date(start.time + 7.days.inWholeMilliseconds),
    disabledValues: List<Date> = emptyList(),
    formatter: DateFormat = SimpleDateFormat("EE'\n'MMM'\n'd", Locale.getDefault()),
) {
    val start = remember(start) {
        (start.time - 7.days.inWholeMilliseconds).coerceAtLeast(Date().time).let(::Date).normalize()
    }
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
    val state = rememberLazyListState()
    LazyRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp),
        state = state
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
    LaunchedEffect(selected, dates) {
        state.animateScrollToItem(dates.indexOf(selected))
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
    val color by animateColorAsState(
        targetValue = when {
            selected -> Theme.color.container.primary
            else -> Theme.color.container.background
        }
    )
    Surface(
        modifier = Modifier.alpha(if (enabled) 1f else .5f),
        shape = Theme.container.button,
        color = color,
        contentColor = Theme.color.contentColorFor(color),
        border = BorderStroke(1.dp, Theme.color.container.outline).takeUnless { selected }
    ) {
        Text(
            modifier = Modifier
                .clickable(enabled = enabled, onClick = onClick.withHaptics())
                .padding(12.dp, 8.dp),
            text = date,
            style = Theme.textStyle.body,
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
