package movie.metropolis.app.screen2.booking.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.metropolis.app.R
import movie.style.layout.PreviewLayout
import movie.style.modifier.glow
import movie.style.modifier.stroke
import movie.style.modifier.surface
import movie.style.shape.CompositeShape
import movie.style.shape.TicketShape
import movie.style.theme.Theme

@Composable
fun TicketColumn(
    color: Color,
    contentColor: Color,
    note: (@Composable () -> Unit)?,
    poster: @Composable () -> Unit,
    metadata: @Composable () -> Unit,
    code: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    var codeSize by remember { mutableStateOf(IntSize.Zero) }
    val baselineShape = RoundedCornerShape(32.dp)
    val innerShape = RoundedCornerShape(24.dp)
    val shape = CompositeShape {
        setBaseline(baselineShape)
        addShape(TicketShape(12.dp, codeSize.height))
    }
    Column(
        modifier = modifier
            .surface(color, shape, 0.dp, color)
            .glow(shape, contentColor)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            if (note != null) Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .padding(horizontal = 16.dp)
                    .stroke(contentColor, innerShape, 1.dp, 4.dp)
                    .clip(innerShape)
                    .background(contentColor.copy(alpha = .1f))
                    .fillMaxWidth()
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                ProvideTextStyle(Theme.textStyle.caption) {
                    note()
                }
            }
            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .clip(innerShape)
                    .glow(innerShape, contentColor)
                    .fillMaxWidth()
                    .weight(1f),
                propagateMinConstraints = true
            ) {
                poster()
            }
            Box(
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .fillMaxWidth(), propagateMinConstraints = true
            ) {
                metadata()
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .onSizeChanged { codeSize = it },
                propagateMinConstraints = true
            ) {
                code()
            }
        }
    }
}

@Composable
fun TicketMetadata(
    cinema: @Composable () -> Unit,
    date: @Composable () -> Unit,
    time: @Composable () -> Unit,
    hall: @Composable () -> Unit,
    row: @Composable () -> Unit,
    seat: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProvideTextStyle(Theme.textStyle.caption) {
            cinema()
            Row(
                modifier = Modifier.height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconRow(
                    icon = { Icon(painterResource(R.drawable.ic_calendar), null) },
                    text = { date() }
                )
                VerticalDivider(
                    modifier = Modifier
                        .height(8.dp)
                        .alpha(.4f),
                    color = LocalContentColor.current
                )
                IconRow(
                    icon = { Icon(painterResource(R.drawable.ic_clock), null) },
                    text = { time() }
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.Top
            ) {
                hall()
                VerticalDivider(
                    modifier = Modifier
                        .height(8.dp)
                        .align(Alignment.CenterVertically)
                        .alpha(.4f),
                    color = LocalContentColor.current
                )
                row()
                VerticalDivider(
                    modifier = Modifier
                        .height(8.dp)
                        .align(Alignment.CenterVertically)
                        .alpha(.4f),
                    color = LocalContentColor.current
                )
                seat()
            }
        }
    }
}

@Composable
fun IconRow(
    icon: @Composable () -> Unit,
    text: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier,
    horizontalArrangement = Arrangement.spacedBy(4.dp),
    verticalAlignment = Alignment.CenterVertically
) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .alpha(.4f),
        propagateMinConstraints = true
    ) {
        icon()
    }
    text()
}

@Composable
fun TicketMetadataColumn(
    title: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    CompositionLocalProvider(LocalContentColor provides LocalContentColor.current.copy(alpha = .4f)) {
        ProvideTextStyle(Theme.textStyle.caption.copy(fontWeight = FontWeight.Light)) {
            title()
        }
    }
    content()
}

@Preview
@Composable
private fun TicketColumnPreview() = PreviewLayout(
    modifier = Modifier.padding(
        horizontal = 64.dp,
        vertical = 32.dp
    )
) {
    TicketColumn(
        color = Color.Black,
        contentColor = Color.White,
        note = { Text("Note") },
        poster = { Box(Modifier.background(Color.Gray)) },
        metadata = {
            TicketMetadata(
                cinema = { Text("Cinema") },
                date = { Text("Feb 25") },
                time = { Text("12:30") },
                hall = { TicketMetadataColumn({ Text("Hall") }) { Text("5") } },
                row = { TicketMetadataColumn({ Text("Row") }) { Text("16") } },
                seat = { TicketMetadataColumn({ Text("Seat") }) { Text("28") } }
            )
        },
        code = { Box(Modifier.background(Color.Blue)) }
    )
}