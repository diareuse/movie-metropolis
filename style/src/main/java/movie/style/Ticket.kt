package movie.style

import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.PreviewLayout
import movie.style.shape.CompositeShape
import movie.style.shape.TicketShape
import movie.style.theme.Theme

@Composable
fun Ticket(
    poster: @Composable () -> Unit,
    date: @Composable () -> Unit,
    time: @Composable () -> Unit,
    hall: @Composable () -> Unit,
    row: @Composable () -> Unit,
    seat: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    contentPadding: Dp = 16.dp
) {
    var offset by remember { mutableIntStateOf(0) }
    val shape = CompositeShape {
        setBaseline(RoundedCornerShape(cornerRadius + contentPadding))
        addShape(TicketShape(cornerRadius, offset))
    }
    val innerShape = RoundedCornerShape(cornerRadius)
    val density = LocalDensity.current
    Column(
        modifier = modifier
            .clip(shape)
            .background(Theme.color.container.background, shape)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(bottom = cornerRadius / 2)
                .clip(innerShape),
            propagateMinConstraints = true
        ) {
            poster()
        }
        Column(
            modifier = Modifier
                .onSizeChanged { offset = it.height }
                .padding(contentPadding)
                .padding(top = cornerRadius / 2)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Box(Modifier.weight(1f), propagateMinConstraints = true) { date() }
                VerticalDivider()
                Box(Modifier.weight(1f), propagateMinConstraints = true) { time() }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { hall() }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { row() }
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) { seat() }
            }
        }
    }
}

@Composable
fun TicketLocationColumn(
    value: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Column(
    modifier = Modifier
        .clip(CircleShape)
        .then(modifier)
        .padding(8.dp),
    verticalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterVertically),
    horizontalAlignment = Alignment.CenterHorizontally
) {
    ProvideTextStyle(
        MaterialTheme.typography.headlineSmall.copy(
            fontWeight = FontWeight.Medium,
            lineHeight = 1.sp
        )
    ) {
        value()
    }
    ProvideTextStyle(
        value = MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            lineHeight = 1.sp
        )
    ) {
        label()
    }
}

@Composable
fun TicketTimeRow(
    icon: @Composable () -> Unit,
    value: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) = Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
) {
    Box(Modifier.size(20.dp), propagateMinConstraints = true) { icon() }
    ProvideTextStyle(MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)) {
        value()
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
private fun TicketPreview() = PreviewLayout(modifier = Modifier.padding(16.dp)) {
    Ticket(
        modifier = Modifier.background(LocalContentColor.current.copy(alpha = .1f)),
        poster = { Box(Modifier.background(Color.Blue)) },
        date = {
            TicketTimeRow(
                icon = { Icon(Icons.Rounded.DateRange, null) },
                value = { Text("Sept 30") }
            )
        },
        time = {
            TicketTimeRow(
                icon = { Icon(Icons.Rounded.Notifications, null) },
                value = { Text("12:30") }
            )
        },
        hall = { TicketLocationColumn(value = { Text("8") }, label = { Text("Hall") }) },
        row = { TicketLocationColumn(value = { Text("2") }, label = { Text("Row") }) },
        seat = { TicketLocationColumn(value = { Text("8") }, label = { Text("Seat") }) }
    )
}
