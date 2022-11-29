package movie.metropolis.app.screen.booking

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.screen.profile.Barcode
import movie.metropolis.app.theme.Theme
import kotlin.random.Random.Default.nextLong

typealias Row = String
typealias Seat = String

@Composable
fun BookingTicketDialog(
    code: String,
    poster: String,
    hall: String,
    seats: List<Pair<Row, Seat>>,
    time: String,
    name: String,
    isVisible: Boolean,
    onVisibilityChanged: (Boolean) -> Unit
) {
    AppDialog(
        isVisible = isVisible,
        onVisibilityChanged = onVisibilityChanged
    ) {
        val context = LocalContext.current
        val token = remember(isVisible) { nextLong() }
        DisposableEffect(token) {
            val window = context.findActivity().window
            val initialBrightness = window.attributes.screenBrightness
            window.attributes = window.attributes.apply {
                screenBrightness = 1f
            }
            onDispose {
                window.attributes = window.attributes.apply {
                    screenBrightness = initialBrightness
                }
            }
        }
        BookingTicketDialogContent(
            code = code,
            poster = poster,
            hall = hall,
            seats = seats,
            time = time,
            name = name
        )
    }
}

private fun Context.findActivity(): Activity {
    when (this) {
        is Activity -> return this
        is ContextWrapper -> return baseContext.findActivity()
    }
    throw IllegalStateException("Unknown Context $this")
}

@Composable
private fun BookingTicketDialogContent(
    code: String,
    poster: String,
    hall: String,
    seats: List<Pair<Row, Seat>>,
    time: String,
    name: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight()
                .padding(24.dp)
                .shadow(32.dp)
                .clip(TicketShape(CornerSize(16.dp)))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            Box(contentAlignment = Alignment.BottomStart) {
                val density = LocalDensity.current
                val colors = listOf(Color.Transparent, MaterialTheme.colorScheme.surface)
                val brush = Brush.verticalGradient(colors)
                var size by remember { mutableStateOf(IntSize.Zero) }
                AsyncImage(
                    modifier = Modifier
                        .width(with(density) { size.width.toDp() })
                        .height(with(density) { size.height.toDp() })
                        .drawWithContent {
                            drawContent()
                            drawRect(brush)
                        },
                    model = poster,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.TopCenter
                )
                Column(Modifier.onGloballyPositioned { size = it.size }) {
                    Spacer(Modifier.height(200.dp))
                    Text(
                        name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Table(
                        TableRow(
                            "Venue",
                            "Time",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = LocalContentColor.current.copy(alpha = .75f)
                            )
                        ),
                        TableRow(hall, time),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    val rows = buildList {
                        this += TableRow(
                            "Row",
                            "Seat",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = LocalContentColor.current.copy(alpha = .75f)
                            )
                        )
                        for ((row, seat) in seats)
                            this += TableRow(row, seat)
                    }
                    Table(
                        rows = rows,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            val color = MaterialTheme.colorScheme.onSurface
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .offset(y = 2.dp)
                    .alpha(.25f)
                    .drawBehind {
                        drawLine(
                            color,
                            Offset(0f, size.height / 2),
                            Offset(size.width, size.height / 2),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 25f), 25f / 2),
                            strokeWidth = size.height
                        )
                    }
            )
            Barcode(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                code = code,
                format = BarcodeFormat.QR_CODE,
                color = LocalContentColor.current
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        BookingTicketDialogContent(
            code = "3921492517",
            poster = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
            hall = "IMAX",
            seats = listOf("12" to "10", "12" to "11"),
            time = "12:10",
            name = "Wonder Woman"
        )
    }
}

class TicketShape(
    private val cornerSize: CornerSize
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val radius = CornerRadius(cornerSize.toPx(size, density))
        val path = Path()
        path.addRoundRect(RoundRect(0f, 0f, size.width, size.height, radius))
        val cutoutLeft = Path()
        cutoutLeft.addOval(
            Rect(
                center = Offset(0f, size.height - size.width),
                radius.x
            )
        )
        val cutoutRight = Path()
        cutoutRight.addOval(
            Rect(
                center = Offset(
                    size.width,
                    size.height - size.width
                ), radius.x
            )
        )
        path.op(path, cutoutLeft, PathOperation.Difference)
        path.op(path, cutoutRight, PathOperation.Difference)
        return Outline.Generic(path)
    }
}