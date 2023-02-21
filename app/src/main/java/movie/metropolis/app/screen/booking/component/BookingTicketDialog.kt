package movie.metropolis.app.screen.booking.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.geometry.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.*
import androidx.compose.ui.text.style.*
import androidx.compose.ui.tooling.preview.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.style.AppDialog
import movie.style.AppImage
import movie.style.Barcode
import movie.style.layout.TicketShape
import movie.style.modifier.overlay
import movie.style.state.ImmutableList
import movie.style.state.ImmutableList.Companion.immutable
import movie.style.theme.Theme
import movie.style.util.findActivity
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
                    var size by remember { mutableStateOf(IntSize.Zero) }
                    AppImage(
                        modifier = Modifier
                            .width(with(density) { size.width.toDp() })
                            .height(with(density) { size.height.toDp() })
                            .overlay(),
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