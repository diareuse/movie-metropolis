package movie.metropolis.app.screen.profile

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import movie.metropolis.app.theme.Theme
import movie.metropolis.app.view.imagePlaceholder
import movie.metropolis.app.view.textPlaceholder

@Composable
fun MembershipCard(
    firstName: String,
    lastName: String,
    cardNumber: String,
    until: String,
    points: String,
    modifier: Modifier = Modifier,
) {
    MembershipCardLayout(
        modifier = modifier,
        name = { Text("%s %s".format(firstName, lastName)) },
        expiration = { Text("Expires: $until") },
        points = { Text("$points points") },
        barcode = {
            Barcode(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.surface)
                    .height(64.dp)
                    .padding(vertical = 8.dp),
                code = cardNumber,
                format = BarcodeFormat.CODE_128,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    )
}

@Composable
fun MembershipCard(modifier: Modifier = Modifier) {
    MembershipCardLayout(
        modifier = modifier,
        name = { Text("Jonathan Superlongname", modifier = Modifier.textPlaceholder(true)) },
        expiration = { Text("01. 01. 1111", modifier = Modifier.textPlaceholder(true)) },
        points = { Text("1234 points", modifier = Modifier.textPlaceholder(true)) },
        barcode = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.medium)
                    .height(64.dp)
                    .imagePlaceholder(true)
            )
        }
    )
}

@Composable
private fun MembershipCardLayout(
    name: @Composable () -> Unit,
    expiration: @Composable () -> Unit,
    points: @Composable () -> Unit,
    barcode: @Composable () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.tertiary,
        shape = MaterialTheme.shapes.large
    ) {
        Column {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                text = "Club Membership",
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )
            Surface(
                modifier = modifier,
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = MaterialTheme.shapes.large,
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(Modifier.padding(horizontal = 16.dp)) {
                        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.titleLarge) {
                            name()
                        }
                        expiration()
                        points()
                    }
                    Spacer(Modifier.height(16.dp))
                    barcode()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    Theme {
        MembershipCard(
            firstName = "Jonathan",
            lastName = "SuperLongNamer",
            cardNumber = "1234612377231",
            until = "21.5.2053",
            points = "513.2"
        )
    }
}

@Composable
fun Barcode(
    code: String,
    format: BarcodeFormat,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.surface
) {
    val writer = MultiFormatWriter()
    val (size, onSizeChanged) = remember { mutableStateOf(IntSize.Zero) }
    var image by remember { mutableStateOf(null as ImageBitmap?) }
    LaunchedEffect(size, code, format) {
        image = withContext(Dispatchers.Default) {
            val w = size.width
            val h = size.height
            if (w * h <= 0) return@withContext null
            val matrix = writer.encode(code, format, w, h)
            val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            for (x in 0 until matrix.width) for (y in 0 until matrix.height) {
                val pixel = if (matrix.get(x, y)) color.toArgb() else Color.Transparent.toArgb()
                bitmap.setPixel(x, y, pixel)
            }
            bitmap.asImageBitmap()
        }
    }
    Canvas(modifier.onSizeChanged(onSizeChanged)) {
        drawImage(image ?: return@Canvas)
    }
}
