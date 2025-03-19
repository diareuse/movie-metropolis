package movie.metropolis.app.ui.booking.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.util.pc
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

private const val multiplier = 1f

@Composable
fun BookingTable(
    modifier: Modifier = Modifier,
    state: ScrollState = rememberScrollState(),
    rows: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(state)
    ) {
        BookingTableRow({}) {
            repeat(24) {
                BookingTableHead(it)
            }
        }
        rows()
    }
}

@Composable
fun ColumnScope.BookingTableRow(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) = Column(modifier = modifier.width(24.hours.inWholeMinutes.times(multiplier).dp)) {
    title()
    Box {
        content()
    }
}

@Composable
fun BoxScope.BookingBox(
    start: Duration,
    duration: Duration,
    onClick: () -> Unit,
    time: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    BookingTableCell(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, MaterialTheme.shapes.small)
            .clickable(onClick = onClick)
            .padding(vertical = .5.pc, horizontal = .5.pc),
        start = start,
        duration = duration
    ) {
        CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSecondaryContainer) {
            ProvideTextStyle(MaterialTheme.typography.labelMedium) {
                time()
            }
        }
    }
}

@Composable
fun BoxScope.BookingTableHead(
    hour: Int,
    modifier: Modifier = Modifier
) {
    BookingTableCell(
        modifier = modifier,
        start = hour.hours,
        duration = 1.hours
    ) {
        val color = LocalContentColor.current.copy(.5f)
        Box(
            modifier = Modifier
                .drawWithCache {
                    val strokeWidth = 2.dp.toPx()
                    val path = Path().apply {
                        moveTo(0f, size.height / 2)
                        quadraticTo(0f, size.height, size.height / 2, size.height)
                        lineTo(size.width - 1.pc.toPx(), size.height)
                    }
                    onDrawWithContent {
                        drawPath(path, color, style = Stroke(width = strokeWidth))
                        drawContent()
                    }
                }
                .padding(horizontal = .5.pc, vertical = .25.pc),
            contentAlignment = Alignment.CenterStart) {
            Text("${hour}h")
        }
    }
}

@Composable
private fun BoxScope.BookingTableCell(
    start: Duration,
    duration: Duration,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .width(duration.inWholeMinutes.times(multiplier).dp)
            .offset(start.inWholeMinutes.times(multiplier).dp)
            .then(modifier),
        propagateMinConstraints = true
    ) {
        content()
    }
}

@PreviewLightDark
@Composable
private fun BookingTablePreview() = PreviewLayout {
    val state = rememberScrollState()
    val density = LocalDensity.current
    LaunchedEffect(Unit) {
        with(density) {
            state.scrollTo(6.hours.inWholeMinutes.times(multiplier).dp.toPx().toInt())
        }
    }
    BookingTable(
        modifier = Modifier.background(MaterialTheme.colorScheme.background),
        state = state
    ) {
        BookingTableRow({ Text("Title", Modifier.offset { IntOffset(x = state.value, y = 0) }) }) {
            BookingBox(6.hours, 1.5.hours, {}, { Text("06:00") })
        }
    }
}