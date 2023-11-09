package movie.style

import android.graphics.Bitmap
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.*
import androidx.compose.ui.unit.*
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun Barcode(
    code: String,
    modifier: Modifier = Modifier,
    format: BarcodeFormat = BarcodeFormat.QR_CODE,
    color: Color = Color.Black
) {
    val (size, onSizeChanged) = remember { mutableStateOf(IntSize.Zero) }
    val image by rememberBarcodeAsState(color = color, size = size, code = code, format = format)
    val bitmap = image
    if (bitmap != null) BarcodeViewer(
        modifier = modifier.onSizeChanged(onSizeChanged),
        bitmap = bitmap
    )
    else Box(
        modifier = modifier.onSizeChanged(onSizeChanged),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(strokeCap = StrokeCap.Round)
    }
}

@Composable
fun BarcodeViewer(
    bitmap: ImageBitmap,
    modifier: Modifier = Modifier
) {
    Canvas(modifier) {
        drawImage(bitmap)
    }
}

@Composable
fun rememberBarcodeAsState(
    color: Color,
    size: IntSize,
    code: String,
    format: BarcodeFormat
): State<ImageBitmap?> {
    val image = remember { mutableStateOf(null as ImageBitmap?) }
    LaunchedEffect(size, code, format, color) {
        val writer = MultiFormatWriter()
        image.value = withContext(Dispatchers.Default) {
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
    return image
}