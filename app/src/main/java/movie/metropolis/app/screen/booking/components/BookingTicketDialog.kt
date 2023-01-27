package movie.metropolis.app.screen.booking.components

import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.metropolis.app.screen.profile.Barcode
import movie.metropolis.app.util.findActivity
import movie.style.AppDialog
import movie.style.AppImage
import movie.style.layout.TicketShape
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme
import kotlin.random.Random.Default.nextLong

typealias Row = String
typealias Seat = String

@Composable
fun BookingTicketDialog(
    code: String,
    poster: String,
    hall: String,
    seats: ImmutableList<Pair<Row, Seat>>,
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

@Composable
private fun BookingTicketDialogContent(
    code: String,
    poster: String,
    hall: String,
    seats: ImmutableList<Pair<Row, Seat>>,
    time: String,
    name: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .padding(24.dp),
            shadowElevation = 32.dp,
            shape = TicketShape(CornerSize(16.dp)),
            color = Theme.color.container.background,
            contentColor = Theme.color.content.background
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(contentAlignment = Alignment.BottomStart) {
                    val density = LocalDensity.current
                    val colors = listOf(Color.Transparent, Theme.color.container.background)
                    val brush = Brush.verticalGradient(colors)
                    var size by remember { mutableStateOf(IntSize.Zero) }
                    AppImage(
                        modifier = Modifier
                            .width(with(density) { size.width.toDp() })
                            .height(with(density) { size.height.toDp() })
                            .drawWithContent {
                                drawContent()
                                drawRect(brush)
                            },
                        url = poster,
                        alignment = Alignment.TopCenter
                    )
                    Column(Modifier.onGloballyPositioned { size = it.size }) {
                        Spacer(Modifier.height(200.dp))
                        Text(
                            name,
                            style = Theme.textStyle.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Table(
                            TableRow(
                                stringResource(R.string.venue),
                                stringResource(R.string.time),
                                style = Theme.textStyle.caption.copy(
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
                                stringResource(R.string.row),
                                stringResource(R.string.seat),
                                style = Theme.textStyle.caption.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = LocalContentColor.current.copy(alpha = .75f)
                                )
                            )
                            for ((row, seat) in seats)
                                this += TableRow(row, seat)
                        }.immutable()
                        Table(
                            rows = rows,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
                val color = Theme.color.content.background
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
                                pathEffect = PathEffect.dashPathEffect(
                                    floatArrayOf(25f, 25f),
                                    25f / 2
                                ),
                                strokeWidth = size.height
                            )
                        }
                )
                Barcode(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .background(Color.White),
                    code = code,
                    format = BarcodeFormat.QR_CODE,
                    color = Color.Black
                )
            }
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
            seats = listOf("12" to "10", "12" to "11").immutable(),
            time = "12:10",
            name = "Wonder Woman"
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun PreviewDark() {
    Theme {
        BookingTicketDialogContent(
            code = "3921492517",
            poster = "https://www.cinemacity.cz/xmedia-cw/repo/feats/posters/5376O2R-lg.jpg",
            hall = "IMAX",
            seats = listOf("12" to "10", "12" to "11").immutable(),
            time = "12:10",
            name = "Wonder Woman"
        )
    }
}