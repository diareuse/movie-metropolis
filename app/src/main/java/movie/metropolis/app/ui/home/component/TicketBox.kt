package movie.metropolis.app.ui.home.component

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import movie.style.layout.DefaultPosterAspectRatio
import movie.style.layout.PreviewLayout
import movie.style.shape.CompositeShape
import movie.style.shape.TicketShape
import movie.style.util.pc
import movie.style.util.toDpSize

@Composable
fun TicketBox(
    expired: Boolean,
    onClick: () -> Unit,
    date: @Composable () -> Unit,
    time: @Composable () -> Unit,
    poster: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    aspectRatio: Float = DefaultPosterAspectRatio,
    shape: Shape = MaterialTheme.shapes.medium,
    contentColor: Color = LocalContentColor.current,
    color: Color = Color.Black
) {
    var contentSize by remember { mutableStateOf(DpSize.Zero) }
    Card(
        modifier = modifier.widthIn(max = 100.dp),
        onClick = onClick,
        shape = CompositeShape(contentSize) {
            setBaseline(shape)
            addShape(TicketShape(8.dp, contentSize.height))
        }
    ) {
        val density = LocalDensity.current
        Box(
            modifier = Modifier
                .drawWithCache {
                    onDrawWithContent {
                        drawContent()
                        if (expired) drawRect(
                            Color.Gray,
                            size = contentSize.toSize(),
                            blendMode = BlendMode.Saturation,
                            topLeft = Offset(0f, size.height - contentSize.height.toPx())
                        )
                    }
                },
            contentAlignment = Alignment.BottomCenter
        ) {
            Box(
                modifier = Modifier
                    .aspectRatio(aspectRatio)
                    .drawWithCache {
                        onDrawWithContent {
                            drawContent()
                            drawRect(Brush.verticalGradient(listOf(color.copy(0f), color)))
                        }
                    },
                propagateMinConstraints = true
            ) {
                poster()
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged { contentSize = it.toDpSize(density) }
                    .padding(vertical = 1.pc),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProvideTextStyle(MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Black)) {
                    CompositionLocalProvider(LocalContentColor provides contentColor) {
                        date()
                        time()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun TicketBoxPreview() = PreviewLayout {
    TicketBox(
        modifier = Modifier
            .padding(1.pc)
            .width(100.dp),
        expired = false,
        onClick = {},
        date = { Text("Dec 12") },
        time = { Text("18:00") },
        poster = { Box(Modifier.background(Color.Green)) }
    )
}

@Preview
@Composable
private fun TicketBoxExpiredPreview() = PreviewLayout {
    TicketBox(
        modifier = Modifier
            .padding(1.pc)
            .width(100.dp),
        expired = true,
        onClick = {},
        date = { Text("Dec 12") },
        time = { Text("18:00") },
        poster = { Box(Modifier.background(Color.Green)) }
    )
}