package movie.metropolis.app.screen.booking.component

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.res.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import movie.metropolis.app.R
import movie.metropolis.app.model.BookingView
import movie.style.AppIconButton
import movie.style.Barcode
import movie.style.Image
import movie.style.OnClickListener
import movie.style.layout.RibbonOverlayLayout
import movie.style.rememberPaletteImageState
import movie.style.theme.Theme

@Composable
fun TicketItemExpired(
    item: BookingView.Expired,
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
    val state = rememberPaletteImageState(url = item.movie.poster?.url.orEmpty())
    TicketOverlay(
        modifier = modifier.onGloballyPositioned { width = it.size.width },
        cutoutOffset = cutoutOffsetOverride,
        color = state.palette.color,
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
            poster = {
                RibbonOverlayLayout(
                    ribbon = {
                        Text(
                            text = stringResource(id = R.string.expired),
                            modifier = Modifier.padding(8.dp),
                            style = Theme.textStyle.emphasis
                        )
                    }
                ) {
                    Image(state)
                }
            },
            name = { Text(item.name) },
            date = { Text(item.date) },
            metadata = {},
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