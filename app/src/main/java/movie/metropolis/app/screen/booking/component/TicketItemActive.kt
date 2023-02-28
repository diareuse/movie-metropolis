package movie.metropolis.app.screen.booking.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.style.AppIconButton
import movie.style.AppImage
import movie.style.Barcode
import movie.style.OnClickListener

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TicketItemActive(
    item: BookingView.Active,
    onShare: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: OnClickListener? = null
) {
    var cutoutOffset by remember { mutableStateOf(0) }
    var width by remember { mutableStateOf(0) }
    var isVisible by remember { mutableStateOf(false) }
    val cutoutOffsetOverride by animateIntAsState(
        targetValue = if (isVisible) width else cutoutOffset,
        label = "cutout-offset-override"
    )
    TicketActions(
        modifier = modifier.onPlaced { width = it.size.width },
        actions = {
            AppIconButton(
                onClick = onShare,
                painter = painterResource(R.drawable.ic_share)
            )
        }
    ) {
        TicketOverlay(
            cutoutOffset = cutoutOffsetOverride,
            color = item.movie.poster?.spotColor ?: LocalContentColor.current,
            overlay = {
                AppIconButton(
                    onClick = { isVisible = !isVisible },
                    painter = painterResource(
                        if (!isVisible) R.drawable.ic_qr
                        else R.drawable.ic_close
                    )
                )
            }
        ) {
            TicketItem(
                modifier = if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier,
                cinema = { Text(item.cinema.name) },
                poster = { AppImage(item.movie.poster?.url, Modifier.fillMaxSize()) },
                name = { Text(item.name) },
                date = { Text(item.date) },
                metadata = {
                    TicketMetadata(
                        modifier = Modifier.padding(top = 16.dp),
                        seats = item.seats.size,
                        hall = { Text(item.hall) },
                        time = { Text(item.time) },
                        row = { Text(item.seats[it].row) },
                        seat = { Text(item.seats[it].seat) }
                    )
                },
                onBottomOffsetChanged = { cutoutOffset = it }
            )
            TicketFlippable(
                modifier = Modifier.align(Alignment.BottomCenter),
                isVisible = isVisible
            ) {
                Barcode(
                    modifier = Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                        .aspectRatio(1f, true),
                    code = item.id,
                    format = BarcodeFormat.QR_CODE,
                    color = Color.Black
                )
            }
        }
    }
}